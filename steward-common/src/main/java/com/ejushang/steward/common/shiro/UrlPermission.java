package com.ejushang.steward.common.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.io.Serializable;

/**
 * User: liubin
 * Date: 13-12-16
 */
public class UrlPermission implements Permission, Serializable {

	private String url;

	public UrlPermission(String url) {
		this.url = url;
	}

    /**
     * shiro内部用到的权限校验
     * @param p
     * @return
     */
	@Override
	public boolean implies(Permission p) {
        if (this == p) return true;
        if (p == null) return false;
        if(p instanceof UrlPermission) {
            UrlPermission otherP = (UrlPermission)p;
            return url.equals(otherP.url);
        } else if(p instanceof WildcardPermission) {
            WildcardPermission otherP = (WildcardPermission)p;
            return url.equals(eliminateBracket(otherP.toString()));
        }
		return false;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UrlPermission that = (UrlPermission) o;

		if (!url.equals(that.url)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return url.hashCode();
	}

    /**
     * 去除字符串里的中括号,调用前:[123],调用后:123
     * @param str
     * @return
     */
    private String eliminateBracket(String str) {
        if(StringUtils.isBlank(str)) return str;
        return str.replaceAll("[\\[\\]]", "");
    }
}
