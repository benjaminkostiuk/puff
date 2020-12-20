package com.unityTest.courseManagement.restApi;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses({
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 405, message = "Method Not Supported"),
        @ApiResponse(code = 409, message = "Database conflict"),
        @ApiResponse(code = 415, message = "MediaType Not Supported")
})
interface BaseApi {
}
