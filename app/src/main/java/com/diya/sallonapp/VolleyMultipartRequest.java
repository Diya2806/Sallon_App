package com.diya.sallonapp;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final Response.Listener<NetworkResponse> mListener;
    private Map<String, String> mHeaders;

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mHeaders = new HashMap<>();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    private final String boundary = "apiclient-" + System.currentTimeMillis();

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // Add string params
            Map<String, String> params = getParams();
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    buildTextPart(bos, entry.getKey(), entry.getValue());
                }
            }

            // Add file params
            Map<String, DataPart> files = getByteData();
            if (files != null && files.size() > 0) {
                for (Map.Entry<String, DataPart> entry : files.entrySet()) {
                    buildFilePart(bos, entry.getKey(), entry.getValue());
                }
            }

            // End boundary
            bos.write(("--" + boundary + "--\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    protected abstract Map<String, DataPart> getByteData() throws AuthFailureError;

    protected void buildTextPart(ByteArrayOutputStream bos, String parameterName, String parameterValue) throws IOException {
        bos.write(("--" + boundary + "\r\n").getBytes());
        bos.write(("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n").getBytes());
        bos.write((parameterValue + "\r\n").getBytes());
    }

    protected void buildFilePart(ByteArrayOutputStream bos, String paramName, DataPart dataFile) throws IOException {
        bos.write(("--" + boundary + "\r\n").getBytes());
        bos.write(("Content-Disposition: form-data; name=\"" +
                paramName + "\"; filename=\"" + dataFile.getFileName() + "\"\r\n").getBytes());
        bos.write(("Content-Type: " + dataFile.getType() + "\r\n\r\n").getBytes());
        bos.write(dataFile.getContent());
        bos.write("\r\n".getBytes());
    }

    public static class DataPart {
        private String fileName;
        private byte[] content;
        private String type;

        public DataPart(String fileName, byte[] content, String type) {
            this.fileName = fileName;
            this.content = content;
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getType() {
            return type;
        }
    }
}
