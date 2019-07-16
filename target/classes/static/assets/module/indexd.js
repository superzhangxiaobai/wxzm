layui.define(["layer", "admind", "element", "contextMenu"], function(n) {
	var e = layui.jquery,
		m = layui.layer,
		d = layui.admind,
		h = layui.element,
		p = layui.contextMenu,
		k = layui.data(d.tableName).cacheTab,
		l = {},
		c = {
			pageTabs: !0,
			maxTabNum: 20,
			openTabCtxMenu: !0,
			cacheTab: void 0 == k ? !0 : k,
			mTabList: [],
			mTabPosition: void 0,
			loadView: function(a) {
				var b = a.menuPath,
					f = a.menuName;
				if (b) {
					if (c.pageTabs) {
						var g = !1;
						e(".layui-layout-admin>.layui-body>.layui-tab>.layui-tab-title>li").each(function() {
							if (e(this).attr("lay-id") === b) return g = !0, !1
						});
						if (!g) {
							if (c.mTabList.length >= c.maxTabNum) {
								m.msg("最多打开" + c.maxTabNum + "个选项卡", {
									icon: 2
								});
								d.activeNav(c.mTabPosition);
								return
							}
							h.tabAdd("admin-pagetabs", {
								id: b,
								title: f ? f : "无标题",
								content: '<iframe lay-id="' + b + '" src="' + b + '" frameborder="0" class="admin-iframe"></iframe>'
							});
							c.mTabList.push(a);
							c.cacheTab && d.putTempData("indexTabs", c.mTabList)
						}
						h.tabChange("admin-pagetabs", b)
					} else f = e(".layui-layout-admin>.layui-body>.admin-iframe"), !f || 0 >= f.length ? e(".layui-layout-admin>.layui-body").html('<iframe lay-id="' + b + '" src="' + b + '" frameborder="0" class="admin-iframe"></iframe>') : (f.attr("lay-id", b), f.attr("src", b)), c.mTabList.splice(0, c.mTabList.length), c.mTabList.push(a), c.cacheTab && d.putTempData("indexTabs", c.mTabList), c.mTabPosition = a.menuPath, c.cacheTab && d.putTempData("tabPosition", c.mTabPosition);
					750 >= d.getPageWidth() && d.flexible(!0)
				} else console.error("url不能为空"), m.msg("url不能为空", {
					icon: 2
				})
			},
			loadHome: function(a) {
				c.loadView({
					menuPath: a.menuPath,
					menuName: a.menuName
				});
				c.pageTabs || d.activeNav(a.menuPath)
			},
			openTab: function(a) {
				var b = a.url,
					d = a.title;
				a.end && (l[b] = a.end);
				c.loadView({
					menuPath: b,
					menuName: d
				})
			},
			closeTab: function(a) {
				h.tabDelete("admin-pagetabs", a)
			},
			loadSetting: function() {
				if (c.cacheTab) {
					var a = d.getTempData("indexTabs");
					if (a) {
						for (var b = d.getTempData("tabPosition"), f = -1, g = 0; g < a.length; g++) c.pageTabs && c.loadView(a[g]), a[g].menuPath == b && (f = g); - 1 != f && setTimeout(function() {
							c.loadView(a[f]);
							c.pageTabs || d.activeNav(b)
						}, 150)
					}
				}
				g = layui.data(d.tableName).openFooter;
				void 0 != g && 0 == g && e("body.layui-layout-body").addClass("close-footer");
				layui.data(d.tableName).tabAutoRefresh && e(".layui-layout-admin>.layui-body>.layui-tab").attr("lay-autoRefresh", "true")
			},
			setTabCache: function(a) {
				layui.data(d.tableName, {
					key: "cacheTab",
					value: a
				});
				(c.cacheTab = a) ? (d.putTempData("indexTabs", c.mTabList), d.putTempData("tabPosition", c.mTabPosition)) : (d.putTempData("indexTabs", []), d.putTempData("tabPosition", void 0))
			},
			closeTabCache: function() {
				d.putTempData("indexTabs", void 0)
			}
		};
	h.on("nav(admin-side-nav)", function(a) {
		var b = e(a);
		a = b.attr("lay-href");
		var f = b.attr("lay-id");
		f || (f = a);
		if (a && "javascript:;" != a) b = b.text().replace(/(^\s*)|(\s*$)/g, ""), c.loadView({
			menuId: f,
			menuPath: a,
			menuName: b
		});
		else if ("true" === e(".layui-layout-admin>.layui-side>.layui-side-scroll>.layui-nav-tree").attr("lay-accordion") && b.parent().hasClass("layui-nav-item")) {
			if (b.parent().hasClass("layui-nav-itemed") || b.parent().hasClass("layui-this")) e(".layui-layout-admin>.layui-side>.layui-side-scroll>.layui-nav .layui-nav-item").removeClass("layui-nav-itemed"), b.parent().addClass("layui-nav-itemed");
			b.trigger("mouseenter")
		} else d.setNavHoverCss(b.parentsUntil(".layui-nav-item").parent().children().eq(0))
	});
	h.on("tab(admin-pagetabs)", function(a) {
		a = e(this).attr("lay-id");
		c.mTabPosition = a;
		c.cacheTab && d.putTempData("tabPosition", c.mTabPosition);
		d.rollPage("auto");
		d.activeNav(a);
		var b = e(".layui-layout-admin>.layui-body>.layui-tab>.layui-tab-content>.layui-tab-item.layui-show .admin-iframe")[0];
		b && (b.style.height = "99%", b.scrollWidth, b.style.height = "100%");
		b.focus();
		"true" === e(".layui-layout-admin>.layui-body>.layui-tab").attr("lay-autoRefresh") && d.refresh(a)
	});
	h.on("tabDelete(admin-pagetabs)", function(a) {
		var b = c.mTabList[a.index].menuPath;
		c.mTabList.splice(a.index, 1);
		c.cacheTab && d.putTempData("indexTabs", c.mTabList);
		l[b] && l[b].call();
		0 >= e(".layui-layout-admin>.layui-body>.layui-tab>.layui-tab-title>li.layui-this").length && e(".layui-layout-admin>.layui-body>.layui-tab>.layui-tab-title>li:last").trigger("click")
	});
	k = layui.data(d.tableName).openTab;
	void 0 != k && (c.pageTabs = k);
	e("body").off("click.navMore").on("click.navMore", "[nav-bind]", function() {
		var a = e(this).attr("nav-bind");
		e('ul[lay-filter="admin-side-nav"]').addClass("layui-hide");
		e('ul[nav-id="' + a + '"]').removeClass("layui-hide");
		750 >= d.getPageWidth() && d.flexible(!1);
		e(".layui-layout-admin>.layui-header>.layui-nav .layui-nav-item").removeClass("layui-this");
		e(this).parent(".layui-nav-item").addClass("layui-this")
	});
	if (c.openTabCtxMenu && c.pageTabs) e(".layui-layout-admin>.layui-body>.layui-tab>.layui-tab-title").off("contextmenu.tab").on("contextmenu.tab", "li", function(a) {
		var b = e(this).attr("lay-id");
		p.show([{
			icon: "layui-icon layui-icon-refresh",
			name: "刷新当前",
			click: function() {
				h.tabChange("admin-pagetabs", b);
				var a = e(".layui-layout-admin>.layui-body>.layui-tab").attr("lay-autoRefresh");
				a && "true" === a || d.refresh(b)
			}
		}, {
			icon: "layui-icon layui-icon-close-fill ctx-ic-lg",
			name: "关闭当前",
			click: function() {
				d.closeThisTabs(b)
			}
		}, {
			icon: "layui-icon layui-icon-unlink",
			name: "关闭其他",
			click: function() {
				d.closeOtherTabs(b)
			}
		}, {
			icon: "layui-icon layui-icon-close ctx-ic-lg",
			name: "关闭全部",
			click: function() {
				d.closeAllTabs()
			}
		}], a.clientX, a.clientY);
		return !1
	});
	n("indexd", c)
});