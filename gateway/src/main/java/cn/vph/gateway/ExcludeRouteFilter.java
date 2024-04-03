package cn.vph.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

//@Component
public class ExcludeRouteFilter extends AbstractGatewayFilterFactory<ExcludeRouteFilter.Config> {

    public ExcludeRouteFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String requestPath = exchange.getRequest().getURI().getPath();
            if (config.shouldFilter(requestPath)) {
                // 返回false表示该请求应该被过滤
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        private String[] excludedPaths;

        public String[] getExcludedPaths() {
            return excludedPaths;
        }

        public void setExcludedPaths(String[] excludedPaths) {
            this.excludedPaths = excludedPaths;
        }

        public boolean shouldFilter(String path) {
            // 在这里根据需求实现排除逻辑，比如匹配路径等等
            for (String excludedPath : excludedPaths) {
                if (path.equals(excludedPath)) {
                    return true;
                }
            }
            return false;
        }
    }
}
