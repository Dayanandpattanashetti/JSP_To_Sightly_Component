<%@page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %>
<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling" %>
<%@include file="/libs/foundation/global.jsp" %>

<cq:include script="/libs/wcm/core/components/init/init.jsp" />

<%
    ValueMap properties = resource.adaptTo(ValueMap.class);
    String title = properties.get("title", "Default Title");
    String description = properties.get("description", "Default Description");
    String theme = properties.get("theme", "light");
    List<Map<String, String>> products = new ArrayList<>();
    
    Resource productsResource = resource.getChild("products");
    if (productsResource != null) {
        for (Resource product : productsResource.getChildren()) {
            ValueMap productProps = product.getValueMap();
            Map<String, String> productData = new HashMap<>();
            productData.put("image", productProps.get("image", ""));
            productData.put("title", productProps.get("title", "No Title"));
            productData.put("description", productProps.get("description", "No Description"));
            productData.put("price", productProps.get("price", "N/A"));
            productData.put("discount", productProps.get("discount", "0"));
            products.add(productData);
        }
    }

    // Fetch OSGi Configuration
    String defaultCurrency = "USD";
    String enableDiscounts = "false";
    
    org.osgi.service.cm.ConfigurationAdmin configAdmin = sling.getService(org.osgi.service.cm.ConfigurationAdmin.class);
    if (configAdmin != null) {
        org.osgi.service.cm.Configuration config = configAdmin.getConfiguration("com.myproject.core.config.ProductConfig");
        if (config != null) {
            defaultCurrency = (String) config.getProperties().getOrDefault("defaultCurrency", "USD");
            enableDiscounts = (String) config.getProperties().getOrDefault("enableDiscounts", "false");
        }
    }
%>

<div class="product-showcase theme-<%= theme %>" data-component="product-showcase">
    <h2><%= title %></h2>
    <p><%= description %></p>
    
    <div class="products-list">
        <ul>
            <% for (Map<String, String> product : products) { %>
                <li class="product-item">
                    <img src="<%= product.get("image") %>" alt="<%= product.get("title") %>" />
                    <h3><%= product.get("title") %></h3>
                    <p><%= product.get("description") %></p>
                    <span class="price">Original: <s><%= product.get("price") %> <%= defaultCurrency %></s></span>
                    <% if ("true".equals(enableDiscounts) && !"0".equals(product.get("discount"))) { %>
                        <span class="discounted-price">Discounted: <%= Integer.parseInt(product.get("price")) - Integer.parseInt(product.get("discount")) %> <%= defaultCurrency %></span>
                    <% } %>
                </li>
            <% } %>
        </ul>
    </div>
</div>

<%-- Including Client Libraries --%>
<cq:includeClientLib categories="myproject.productshowcase" />
