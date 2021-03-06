/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.dao.support;

import java.util.Iterator;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * AOP Alliance MethodInterceptor that provides persistence exception translation
 * based on a given PersistenceExceptionTranslator.
 *
 * <p>Delegates to the given {@link PersistenceExceptionTranslator} to translate
 * a RuntimeException thrown into Spring's DataAccessException hierarchy
 * (if appropriate). If the RuntimeException in question is declared on the
 * target method, it is always propagated as-is (with no translation applied).
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 2.0
 * @see PersistenceExceptionTranslator
 */
public class PersistenceExceptionTranslationInterceptor
		implements MethodInterceptor, BeanFactoryAware, InitializingBean {

	private PersistenceExceptionTranslator persistenceExceptionTranslator;


	/**
	 * Create a new PersistenceExceptionTranslationInterceptor.
	 * Needs to be configured with a PersistenceExceptionTranslator afterwards.
	 * @see #setPersistenceExceptionTranslator
	 */
	public PersistenceExceptionTranslationInterceptor() {
	}

	/**
	 * Create a new PersistenceExceptionTranslationInterceptor
	 * for the given PersistenceExceptionTranslator.
	 * @param persistenceExceptionTranslator the PersistenceExceptionTranslator to use
	 */
	public PersistenceExceptionTranslationInterceptor(PersistenceExceptionTranslator persistenceExceptionTranslator) {
		setPersistenceExceptionTranslator(persistenceExceptionTranslator);
	}

	/**
	 * Create a new PersistenceExceptionTranslationInterceptor, autodetecting
	 * PersistenceExceptionTranslators in the given BeanFactory.
	 * @param beanFactory the ListableBeanFactory to obtaining all
	 * PersistenceExceptionTranslators from
	 */
	public PersistenceExceptionTranslationInterceptor(ListableBeanFactory beanFactory) {
		this.persistenceExceptionTranslator = detectPersistenceExceptionTranslators(beanFactory);
	}


	/**
	 * Specify the PersistenceExceptionTranslator to use.
	 * <p>Default is to autodetect all PersistenceExceptionTranslators
	 * in the containing BeanFactory, using them in a chain.
	 * @see #detectPersistenceExceptionTranslators
	 */
	public void setPersistenceExceptionTranslator(PersistenceExceptionTranslator pet) {
		Assert.notNull(pet, "PersistenceExceptionTranslator must not be null");
		this.persistenceExceptionTranslator = pet;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (this.persistenceExceptionTranslator == null) {
			// No explicit exception translator specified - perform autodetection.
			if (!(beanFactory instanceof ListableBeanFactory)) {
				throw new IllegalArgumentException(
						"Cannot use PersistenceExceptionTranslator autodetection without ListableBeanFactory");
			}
			this.persistenceExceptionTranslator =
					detectPersistenceExceptionTranslators((ListableBeanFactory) beanFactory);
		}
	}

	public void afterPropertiesSet() {
		if (this.persistenceExceptionTranslator == null) {
			throw new IllegalArgumentException("Property 'persistenceExceptionTranslator' is required");
		}
	}


	/**
	 * Detect all PersistenceExceptionTranslators in the given BeanFactory.
	 * @param beanFactory the ListableBeanFactory to obtaining all
	 * PersistenceExceptionTranslators from
	 * @return a chained PersistenceExceptionTranslator, combining all
	 * PersistenceExceptionTranslators found in the factory
	 * @see ChainedPersistenceExceptionTranslator
	 */
	protected PersistenceExceptionTranslator detectPersistenceExceptionTranslators(ListableBeanFactory beanFactory) {
		// Find all translators, being careful not to activate FactoryBeans.
		Map pets = BeanFactoryUtils.beansOfTypeIncludingAncestors(
				beanFactory, PersistenceExceptionTranslator.class, false, false);
		if (pets.isEmpty()) {
			throw new IllegalStateException(
					"No persistence exception translators found in bean factory. Cannot perform exception translation.");
		}
		ChainedPersistenceExceptionTranslator cpet = new ChainedPersistenceExceptionTranslator();
		for (Iterator it = pets.values().iterator(); it.hasNext();) {
			cpet.addDelegate((PersistenceExceptionTranslator) it.next());
		}
		return cpet;
	}


	public Object invoke(MethodInvocation mi) throws Throwable {
		try {
			return mi.proceed();
		}
		catch (RuntimeException ex) {
			// Let it throw raw if the type of the exception is on the throws clause of the method.
			if (ReflectionUtils.declaresException(mi.getMethod(), ex.getClass())) {
				throw ex;
			}
			else {
				throw DataAccessUtils.translateIfNecessary(ex, this.persistenceExceptionTranslator);
			}
		}
	}

}
