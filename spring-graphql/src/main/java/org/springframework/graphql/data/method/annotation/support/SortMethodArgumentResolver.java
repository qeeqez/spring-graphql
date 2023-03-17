/*
 * Copyright 2020-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.graphql.data.method.annotation.support;


import graphql.schema.DataFetchingEnvironment;

import org.springframework.core.MethodParameter;
import org.springframework.graphql.data.method.HandlerMethodArgumentResolver;
import org.springframework.graphql.data.pagination.SortStrategy;
import org.springframework.util.Assert;


/**
 * Resolver for a Sort object decoded with {@link SortStrategy}.
 *
 * @author Rossen Stoyanchev
 * @since 1.2
 */
public class SortMethodArgumentResolver implements HandlerMethodArgumentResolver {

	private final SortStrategy<?> sortStrategy;


	public SortMethodArgumentResolver(SortStrategy<?> sortStrategy) {
		Assert.notNull(sortStrategy, "SortStrategy is required");
		this.sortStrategy = sortStrategy;
	}


	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return this.sortStrategy.supports(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, DataFetchingEnvironment environment) {
		return this.sortStrategy.extract(environment);
	}

}
