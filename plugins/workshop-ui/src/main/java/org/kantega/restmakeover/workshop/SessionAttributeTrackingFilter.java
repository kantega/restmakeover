package org.kantega.restmakeover.workshop;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 */
public class SessionAttributeTrackingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        chain.doFilter(new HttpServletRequestWrapper(req) {
            @Override
            public HttpSession getSession() {
                return new SessionAttributeTrackingHttpSession(super.getSession(), res);
            }

            @Override
            public HttpSession getSession(boolean create) {
                return new SessionAttributeTrackingHttpSession(super.getSession(create), res);
            }
        }, response);
    }

    @Override
    public void destroy() {

    }

    static class SessionAttributeTrackingHttpSession implements HttpSession {

        private final HttpSession session;
        private final HttpServletResponse res;
        private final Set<String> writtenHeaders = new TreeSet<>();
        private final Set<String> readHeaders = new TreeSet<>();

        public SessionAttributeTrackingHttpSession(HttpSession session, HttpServletResponse res) {

            this.session = session;
            this.res = res;

        }

        @Override
        public Object getAttribute(String name) {
            readHeaders.add(name);
            res.setHeader("X-Session-Attr-Read", readHeaders.toString());
            return session.getAttribute(name);
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return session.getAttributeNames();
        }

        @Override
        public long getCreationTime() {
            return session.getCreationTime();
        }

        @Override
        public String getId() {
            return session.getId();
        }

        @Override
        public long getLastAccessedTime() {
            return session.getLastAccessedTime();
        }

        @Override
        public int getMaxInactiveInterval() {
            return session.getMaxInactiveInterval();
        }

        @Override
        public ServletContext getServletContext() {
            return session.getServletContext();
        }

        @Override
        public HttpSessionContext getSessionContext() {
            return session.getSessionContext();
        }

        @Override
        public Object getValue(String name) {
            return session.getValue(name);
        }

        @Override
        public String[] getValueNames() {
            return session.getValueNames();
        }

        @Override
        public void invalidate() {
            session.invalidate();
        }

        @Override
        public boolean isNew() {
            return session.isNew();
        }

        @Override
        public void putValue(String name, Object value) {
            session.putValue(name, value);
        }

        @Override
        public void removeAttribute(String name) {
            session.removeAttribute(name);
        }

        @Override
        public void removeValue(String name) {
            session.removeValue(name);
        }

        @Override
        public void setAttribute(String name, Object value) {
            session.setAttribute(name, value);
            writtenHeaders.add(name);
            res.setHeader("X-Session-Attr-Write", writtenHeaders.toString());
        }

        @Override
        public void setMaxInactiveInterval(int interval) {
            session.setMaxInactiveInterval(interval);
        }
    }
}
