/*
 * Copyright 2014-2023 the original author or authors.
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

package org.springframework.restdocs.request;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;

import org.springframework.restdocs.snippet.SnippetException;
import org.springframework.restdocs.templates.TemplateFormats;
import org.springframework.restdocs.testfixtures.OperationBuilder;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

/**
 * Tests for failures when rendering {@link FormParametersSnippet} due to missing or
 * undocumented form parameters.
 *
 * @author Andy Wilkinson
 */
public class FormParametersSnippetFailureTests {

	@Rule
	public OperationBuilder operationBuilder = new OperationBuilder(TemplateFormats.asciidoctor());

	@Test
	public void undocumentedParameter() {
		assertThatExceptionOfType(SnippetException.class)
			.isThrownBy(() -> new FormParametersSnippet(Collections.<ParameterDescriptor>emptyList())
				.document(this.operationBuilder.request("http://localhost").content("a=alpha").build()))
			.withMessage("Form parameters with the following names were not documented: [a]");
	}

	@Test
	public void missingParameter() {
		assertThatExceptionOfType(SnippetException.class)
			.isThrownBy(() -> new FormParametersSnippet(Arrays.asList(parameterWithName("a").description("one")))
				.document(this.operationBuilder.request("http://localhost").build()))
			.withMessage("Form parameters with the following names were not found in the request: [a]");
	}

	@Test
	public void undocumentedAndMissingParameters() {
		assertThatExceptionOfType(SnippetException.class)
			.isThrownBy(() -> new FormParametersSnippet(Arrays.asList(parameterWithName("a").description("one")))
				.document(this.operationBuilder.request("http://localhost").content("b=bravo").build()))
			.withMessage("Form parameters with the following names were not documented: [b]. Form parameters"
					+ " with the following names were not found in the request: [a]");
	}

}
