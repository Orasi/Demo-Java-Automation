package com.orasi.api.restServices;

public class ResponseCodes {
    /*
     * 1xx Informational
     */
    public static final int CONTINUE = 100;
    public static final int SWITCHING_PROTOCOLS = 101;
    public static final int PROCESSING = 102;

    /*
     * 2xx Success
     */
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    public static final int NOT_AUTHORIZED = 203;
    public static final int NO_CONTENT = 204;
    public static final int RESET_CONTENT = 205;

    /*
     * 3xx Redirection
     */
    public static final int FOUND = 302;
    public static final int NOT_MODIFIED = 304;

    /*
     * 4xx Client Error
     */
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int PAYMENT_REQUIRED = 402;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int NOT_ACCEPTABLE = 406;
    public static final int PROXY_AUTHENTICATION_REQUIRED = 407;
    public static final int REQUEST_TIMED_OUT = 408;
    public static final int CONFLICT = 409;

    /*
     * 5xx Server Error
     */
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int NOT_IMPLEMENTED = 501;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILIBLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;
}
