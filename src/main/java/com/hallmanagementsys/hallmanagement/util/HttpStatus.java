package com.hallmanagementsys.hallmanagement.util;

public interface HttpStatus {
    int CONTINUE = 100;
    int SWITCHING_PROTOCOLS = 101;
    int PROCESSING = 102;

    int OK = 200;
    int CREATED = 201;
    int ACCEPTED = 202;
    int NON_AUTHORITATIVE_INFORMATION = 203;
    int NO_CONTENT = 204;
    int RESET_CONTENT = 205;
    int PARTIAL_CONTENT = 206;
    int MULTI_STATUS = 207;
    int ALREADY_REPORTED = 208;
    int IM_USED = 226;

    int MULTIPLE_CHOICES = 300;
    int MOVED_PERMANENTLY = 301;
    int FOUND = 302;
    int SEE_OTHER = 303;
    int NOT_MODIFIED = 304;
    int USE_PROXY = 305;
    int TEMPORARY_REDIRECT = 307;
    int PERMANENT_REDIRECT = 308;

    int BAD_REQUEST = 400;
    int UNAUTHORIZED = 401;
    int PAYMENT_REQUIRED = 402;
    int FORBIDDEN = 403;
    int NOT_FOUND = 404;
    int METHOD_NOT_ALLOWED = 405;
    int NOT_ACCEPTABLE = 406;
    int PROXY_AUTHENTICATION_REQUIRED = 407;
    int REQUEST_TIMEOUT = 408;
    int CONFLICT = 409;
    int GONE = 410;
    int LENGTH_REQUIRED = 411;
    int PRECONDITION_FAILED = 412;
    int PAYLOAD_TOO_LARGE = 413;
    int URI_TOO_LONG = 414;
    int UNSUPPORTED_MEDIA_TYPE = 415;
    int RANGE_NOT_SATISFIABLE = 416;
    int EXPECTATION_FAILED = 417;
    int I_AM_A_TEAPOT = 418;
    int UNPROCESSABLE_ENTITY = 422;
    int TOO_MANY_REQUESTS = 429;

    int INTERNAL_SERVER_ERROR = 500;
    int NOT_IMPLEMENTED = 501;
    int BAD_GATEWAY = 502;
    int SERVICE_UNAVAILABLE = 503;
    int GATEWAY_TIMEOUT = 504;
    int HTTP_VERSION_NOT_SUPPORTED = 505;
}

