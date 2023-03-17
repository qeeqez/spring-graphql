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
import org.springframework.graphql.data.pagination.CursorStrategy;
import org.springframework.graphql.data.pagination.PaginationRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Resolver for a method argument of type {@link PaginationRequest} initialized
 * from "first", "last", "before", and "after" GraphQL arguments.
 *
 * @author Rossen Stoyanchev
 * @since 1.2
 */
public class PaginationRequestMethodArgumentResolver<P> implements HandlerMethodArgumentResolver {

	private final CursorStrategy<P> cursorStrategy;


	public PaginationRequestMethodArgumentResolver(CursorStrategy<P> cursorStrategy) {
		Assert.notNull(cursorStrategy, "CursorStrategy is required");
		this.cursorStrategy = cursorStrategy;
	}


	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return (parameter.getParameterType().equals(PaginationRequest.class) &&
				this.cursorStrategy.supports(parameter.nested().getNestedParameterType()));
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, DataFetchingEnvironment environment) throws Exception {
		boolean forward = !environment.getArguments().containsKey("last");
		String cursor = environment.getArgument(forward ? "before" : "after");
		Integer count = environment.getArgument(forward ? "first" : "last");
		P position = (cursor != null ? this.cursorStrategy.fromCursor(cursor) : null);
		return createRequest(position, count, forward);
	}

	/**
	 * Create the {@code PaginationRequest} instance.
	 */
	protected PaginationRequest<P> createRequest(@Nullable P position, @Nullable Integer size, boolean forward) {
		return new PaginationRequest<>(position, size, forward);
	}

}
