/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

declare type TStringFn = (str: string) => string;

/**
 * Format string removing spaces and special characters
 */
export declare const removeAllSpecialCharacters: TStringFn;

/**
 * Transform first letter in lowercase
 */
export declare const firstLetterLowercase: TStringFn;

/**
 * Transform first letter in uppercase
 */
export declare const firstLetterUppercase: TStringFn;

/**
 * Normalize languageId to be used in the
 * frontend with themeDisplay.getDefaultLanguageId()
 */
export declare const normalizeLanguageId: TStringFn;
export {};
