/**
 * 测试前，关闭spring security
 * http.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.anyExchange().permitAll())
 *                 .csrf(ServerHttpSecurity.CsrfSpec::disable)
 *                 .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
 *                 .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
 *                 .logout(ServerHttpSecurity.LogoutSpec::disable);
 */

package org.lizhao.cloud.gateway.controller;