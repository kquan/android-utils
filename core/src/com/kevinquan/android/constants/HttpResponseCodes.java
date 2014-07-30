/*
 * Copyright 2014 Kevin Quan (kevin.quan@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kevinquan.android.constants;

/**
 * List of HTTP Response codes with literal descriptions.
 * 
 * Reference is from: http://en.wikipedia.org/wiki/List_of_HTTP_status_codes
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public interface HttpResponseCodes {
    
    // Informational
    public static final int CONTINUE = 100;
    public static final int SWITCH_PROTOCOLS = 101;
    public static final int PROCESSING = 102;
    
    // Success
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    public static final int NON_AUTHORITATIVE_INFORMATION = 203;
    public static final int NO_CONTENT = 204;
    public static final int RESET_CONTENT = 205;
    public static final int PARTIAL_CONTENT = 206;
    public static final int MULTI_STATUS = 207;
    public static final int ALREADY_REPORTED = 208;
    public static final int IM_USED = 226;
    
    // Redirection
    public static final int MULTIPLE_CHOICES = 300;
    public static final int MOVED_PERMANENTLY = 301;
    public static final int FOUND = 302;
    public static final int SEE_OTHER = 303;
    public static final int NOT_MODIFIED = 304;
    public static final int USE_PROXY = 305;
    @Deprecated
    public static final int SWITCH_PROXY = 306;
    public static final int TEMPORARY_REDIRECT = 307;
    public static final int PERMANENT_REDIRECT = 308;
    
    // Client Error
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int PAYMENT_REQURIED = 402;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    // Others TBA
    
    // Server Errors
    public static final int ERROR = 500;
    public static final int NOT_IMPLEMENTED = 501;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;
    // Others TBA
}
