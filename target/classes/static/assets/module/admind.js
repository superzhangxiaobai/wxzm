layui.define(["layer"], function(l) {
	var b = layui.jquery,
		e = layui.layer,
		d = {
			defaultTheme: "admin",
			tableName: "easyweb",
			flexible: function(a) {
				b(".layui-layout-admin").hasClass("admin-nav-mini") != !a && (a ? b(".layui-layout-admin").removeClass("admin-nav-mini") : b(".layui-layout-admin").addClass("admin-nav-mini"), d.removeNavHover())
			},
			activeNav: function(a) {
				a || (a = window.location.pathname, a = a.split("/"), a = a[a.length - 1]);
				a && "" != a ? (b(".layui-layout-admin>.layui-side>.layui-side-scroll>.layui-nav .layui-nav-item .layui-nav-child dd").removeClass("layui-this"), b(".layui-layout-admin>.layui-side>.layui-side-scroll>.layui-nav .layui-nav-item").removeClass("layui-this"), (a = b('.layui-layout-admin>.layui-side>.layui-side-scroll>.layui-nav a[lay-href="' + a + '"]')) && 0 < a.length && ("true" == b(".layui-layout-admin>.layui-side>.layui-side-scroll>.layui-nav").attr("lay-accordion") && b(".layui-layout-admin>.layui-side>.layui-side-scroll>.layui-nav .layui-nav-item").removeClass("layui-nav-itemed"), a.parent().addClass("layui-this"), a.parent("dd").parents(".layui-nav-child").parent().addClass("layui-nav-itemed"), b('ul[lay-filter="admin-side-nav"]').addClass("layui-hide"), a = a.parents(".layui-nav"), a.removeClass("layui-hide"), b(".layui-layout-admin>.layui-header>.layui-nav>.layui-nav-item").removeClass("layui-this"), b('.layui-layout-admin>.layui-header>.layui-nav>.layui-nav-item>a[nav-bind="' + a.attr("nav-id") + '"]').parent().addClass("layui-this"))) : console.warn("active url not be null")
			},
			popupRight: function(a) {
				void 0 == a.title && (a.title = !1, a.closeBtn = !1);
				void 0 == a.anim && (a.anim = 2);
				void 0 == a.fixed && (a.fixed = !0);
				a.isOutAnim = !1;
				a.offset = "r";
				a.shadeClose = !0;
				a.area = "336px";
				a.skin = "layui-layer-adminRight";
				a.move = !1;
				return d.open(a)
			},
			open: function(a) {
				a.area || (a.area = 2 == a.type ? ["360px", "300px"] : "360px");
				a.skin || (a.skin = "layui-layer-admin");
				a.offset || (a.offset = "35px");
				void 0 == a.fixed && (a.fixed = !1);
				a.resize = void 0 != a.resize ? a.resize : !1;
				a.shade = void 0 != a.shade ? a.shade : .1;
				var c = a.end;
				a.end = function() {
					e.closeAll("tips");
					c && c()
				};
				return e.open(a)
			},
			req: function(a, c, b, g) {
				d.ajax({
					url: a,
					data: c,
					type: g,
					dataType: "json",
					success: b
				})
			},
			ajax: function(a) {
				var c = a.success;
				a.success = function(b, g, e) {
					var f;
					(f = "json" == a.dataType.toLowerCase() ? b : d.parseJSON(b)) && 0 == d.ajaxSuccessBefore(f) || c(b, g, e)
				};
				a.error = function(b) {
					a.success({
						code: b.status,
						msg: b.statusText
					})
				};
				a.beforeSend = function(a) {
					for (var b = d.getAjaxHeaders(), c = 0; c < b.length; c++) a.setRequestHeader(b[c].name, b[c].value)
				};
				b.ajax(a)
			},
			ajaxSuccessBefore: function(a) {
				if (401 == a.code) return e.msg(a.msg, {
					icon: 2,
					time: 1500
				}, function() {}, 1E3), !1
			},
			getAjaxHeaders: function() {
				return []
			},
			parseJSON: function(a) {
				if ("string" == typeof a) try {
					var b = JSON.parse(a);
					if ("object" == typeof b && b) return b
				} catch (f) {
					console.warn(f)
				}
			},
			showLoading: function(a) {
				a || (a = "body");
				b(a).addClass("page-no-scroll");
				b(a).append('<div class="page-loading"><div class="rubik-loader"></div></div>')
			},
			removeLoading: function(a) {
				a || (a = "body");
				b(a).children(".page-loading").remove();
				b(a).removeClass("page-no-scroll")
			},
			putTempData: function(a, b) {
				void 0 != b && null != b ? layui.sessionData("tempData", {
					key: a,
					value: b
				}) : layui.sessionData("tempData", {
					key: a,
					remove: !0
				})
			},
			getTempData: function(a) {
				return layui.sessionData("tempData")[a]
			},
			rollPage: function(a) {
				var c = b(".layui-layout-admin>.layui-body>.layui-tab>.layui-tab-title"),
					d = c.scrollLeft();
				if ("left" === a) c.animate({
					scrollLeft: d - 120
				}, 100);
				else if ("auto" === a) {
					var e = 0;
					c.children("li").each(function() {
						if (b(this).hasClass("layui-this")) return !1;
						e += b(this).outerWidth()
					});
					c.animate({
						scrollLeft: e - 120
					}, 100)
				} else c.animate({
					scrollLeft: d + 120
				}, 100)
			},
			refresh: function(a) {
				var c = a ? b('.layui-layout-admin>.layui-body>.layui-tab>.layui-tab-content>.layui-tab-item>.admin-iframe[lay-id="' + a + '"]') : b(".layui-layout-admin>.layui-body>.layui-tab>.layui-tab-content>.layui-tab-item.layui-show>.admin-iframe");
				if (!c || 0 >= c.length) c = b(".layui-layout-admin>.layui-body>.admin-iframe");
				c && c[0] ? c[0].contentWindow.location.reload(!0) : console.warn(a + " is not found")
			},
			closeThisTabs: function(a) {
				d.closeTabOperNav();
				var c = b(".layui-layout-admin>.layui-body>.layui-tab>.layui-tab-title");
				a ? a == c.find("li").first().attr("lay-id") ? e.msg("主页不能关闭", {
					icon: 2
				}) : c.find('li[lay-id="' + a + '"]').find(".layui-tab-close").trigger("click") : c.find("li").first().hasClass("layui-this") ? e.msg("主页不能关闭", {
					icon: 2
				}) : c.find("li.layui-this").find(".layui-tab-close").trigger("click")
			},
			closeOtherTabs: function(a) {
				a ? b(".layui-layout-admin>.layui-body>.layui-tab>.layui-tab-title li:gt(0)").each(function() {
					a != b(this).attr("lay-id") && b(this).find(".layui-tab-close").trigger("click")
				}) : b(".layui-layout-admin>.layui-body>.layui-tab>.layui-tab-title li:gt(0):not(.layui-this)").find(".layui-tab-close").trigger("click");
				d.closeTabOperNav()
			},
			closeAllTabs: function() {
				b(".layui-layout-admin>.layui-body>.layui-tab>.layui-tab-title li:gt(0)").find(".layui-tab-close").trigger("click");
				b(".layui-layout-admin>.layui-body>.layui-tab>.layui-tab-title li:eq(0)").trigger("click");
				d.closeTabOperNav()
			},
			closeTabOperNav: function() {
				b(".layui-icon-down .layui-nav .layui-nav-child").removeClass("layui-show")
			},
			changeTheme: function(a) {
				a ? (layui.data(d.tableName, {
					key: "theme",
					value: a
				}), "admin" == a && (a = void 0)) : layui.data(d.tableName, {
					key: "theme",
					remove: !0
				});
				d.removeTheme(top);
				!a || top.layui.link(d.getThemeDir() + a + ".css", a);
				for (var b = top.window.frames, f = 0; f < b.length; f++) {
					var e = b[f];
					try {
						d.removeTheme(e)
					} catch (m) {}
					a && e.layui && e.layui.link(d.getThemeDir() + a + ".css", a)
				}
			},
			removeTheme: function(a) {
				a || (a = window);
				a.layui && a.layui.jquery('link[id^="layuicss-theme"]').remove()
			},
			getThemeDir: function() {
				return layui.cache.base + "theme/"
			},
			closeThisDialog: function() {
				parent.layer.close(parent.layer.getFrameIndex(window.name))
			},
			closeDialog: function(a) {
				a = b(a).parents(".layui-layer").attr("id").substring(11);
				e.close(a)
			},
			iframeAuto: function() {
				parent.layer.iframeAuto(parent.layer.getFrameIndex(window.name))
			},
			getPageHeight: function() {
				return document.documentElement.clientHeight || document.body.clientHeight
			},
			getPageWidth: function() {
				return document.documentElement.clientWidth || document.body.clientWidth
			},
			removeNavHover: function() {
				b(".admin-nav-hover>.layui-nav-child").css({
					top: "auto",
					"max-height": "none",
					overflow: "auto"
				});
				b(".admin-nav-hover").removeClass("admin-nav-hover")
			},
			setNavHoverCss: function(a) {
				var c = b(".admin-nav-hover>.layui-nav-child");
				if (a && 0 < c.length) {
					if (a.offset().top + c.outerHeight() > window.innerHeight) {
						var f = a.offset().top - c.outerHeight() + a.outerHeight();
						50 > f ? (f = d.getPageHeight(), a.offset().top < f / 2 ? c.css({
							top: "50px",
							"max-height": f - 50 + "px",
							overflow: "auto"
						}) : c.css({
							top: a.offset().top,
							"max-height": f - a.offset().top,
							overflow: "auto"
						})) : c.css("top", f)
					} else c.css("top", a.offset().top);
					h = !0
				}
			},
			events: {
				flexible: function(a) {
					a = b(".layui-layout-admin").hasClass("admin-nav-mini");
					d.flexible(a)
				},
				refresh: function() {
					d.refresh()
				},
				back: function() {
					history.back()
				},
				theme: function() {
					var a = b(this).attr("data-url");
					d.popupRight({
						type: 2,
						content: a ? a : "page/tpl/tpl-theme.html"
					})
				},
				note: function() {
					var a = b(this).attr("data-url");
					d.popupRight({
						id: "layer-note",
						title: "便签",
						type: 2,
						closeBtn: !1,
						content: a ? a : "page/tpl/tpl-note.html"
					})
				},
				message: function() {
					var a = b(this).attr("data-url");
					d.popupRight({
						type: 2,
						content: a ? a : "page/tpl/tpl-message.html"
					})
				},
				personalInfo:function(){
					var a = b(this).attr("data-url");
					
				},
				psw: function() {
					var a = b(this).attr("data-url");
					d.open({
						id: "pswForm",
						type: 2,
						title: "修改密码",
						shade: 0,
						content: a ? a : "page/tpl/tpl-password.html"
					})
				},
				logout: function() {
					var a = b(this).attr("data-url");
					e.confirm("确定要退出登录吗？", {
						title: "温馨提示",
						skin: "layui-layer-admin"
					}, function() {
						location.replace(a ? a : "/")
					})
				},
				fullScreen: function(a) {
					a = b(this).find("i");
					if (document.fullscreenElement || document.msFullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement) {
						var c = document.exitFullscreen || document.webkitExitFullscreen || document.mozCancelFullScreen || document.msExitFullscreen;
						c ? c.call(document) : window.ActiveXObject && (c = new ActiveXObject("WScript.Shell"), c.SendKeys("{F11}"));
						a.addClass("layui-icon-screen-full").removeClass("layui-icon-screen-restore")
					} else {
						c = document.documentElement;
						var d = c.requestFullscreen || c.webkitRequestFullscreen || c.mozRequestFullScreen || c.msRequestFullscreen;
						d ? d.call(c) : window.ActiveXObject && (c = new ActiveXObject("WScript.Shell"), c.SendKeys("{F11}"));
						a.addClass("layui-icon-screen-restore").removeClass("layui-icon-screen-full")
					}
				},
				leftPage: function() {
					d.rollPage("left")
				},
				rightPage: function() {
					d.rollPage()
				},
				closeThisTabs: function() {
					d.closeThisTabs()
				},
				closeOtherTabs: function() {
					d.closeOtherTabs()
				},
				closeAllTabs: function() {
					d.closeAllTabs()
				},
				closeDialog: function() {
					d.closeThisDialog()
				},
				closePageDialog: function() {
					d.closeDialog(this)
				}
			}
		};
	b("body").on("click", "*[ew-event]", function() {
		var a = b(this).attr("ew-event");
		(a = d.events[a]) && a.call(this, b(this))
	});
	b(".site-mobile-shade").click(function() {
		d.flexible(!0)
	});
	var h = !1;
	b("body").on("mouseenter", ".layui-layout-admin.admin-nav-mini .layui-side .layui-nav .layui-nav-item>a", function() {
		if (750 < d.getPageWidth()) {
			var a = b(this);
			b(".admin-nav-hover>.layui-nav-child").css("top", "auto");
			b(".admin-nav-hover").removeClass("admin-nav-hover");
			a.parent().addClass("admin-nav-hover");
			if (0 < b(".admin-nav-hover>.layui-nav-child").length) d.setNavHoverCss(a);
			else {
				var c = a.find("cite").text(),
					f = b(".layui-layout-admin .layui-side").css("background-color");
				e.tips(c, a, {
					tips: [2, "rgb(255, 255, 255)" == f ? "#009688" : f],
					time: -1
				})
			}
		}
	}).on("mouseleave", ".layui-layout-admin.admin-nav-mini .layui-side .layui-nav .layui-nav-item>a", function() {
		e.closeAll("tips")
	});
	b("body").on("mouseleave", ".layui-layout-admin.admin-nav-mini .layui-side", function() {
		h = !1;
		setTimeout(function() {
			h || d.removeNavHover()
		}, 500)
	});
	b("body").on("mouseenter", ".layui-layout-admin.admin-nav-mini .layui-side .layui-nav .layui-nav-item.admin-nav-hover .layui-nav-child", function() {
		h = !0
	});
	b("body").on("mouseenter", "*[lay-tips]", function() {
		var a = b(this).attr("lay-tips"),
			c = b(this).attr("lay-direction"),
			d = b(this).attr("lay-bg");
		e.tips(a, this, {
			tips: [c || 3, d || "#333333"],
			time: -1
		})
	}).on("mouseleave", "*[lay-tips]", function() {
		e.closeAll("tips")
	});
	b("body").on("click", "*[ew-href]", function() {
		var a = b(this).attr("ew-href"),
			c = b(this).text();
		top.layui.index.openTab({
			title: c,
			url: a
		})
	});
	if (!layui.contextMenu) b(document).off("click.ctxMenu").on("click.ctxMenu", function() {
		for (var a = top.window.frames, b = 0; b < a.length; b++) {
			var d = a[b];
			try {
				d.layui.jquery(".ctxMenu").remove()
			} catch (g) {}
		}
		top.layui.jquery(".ctxMenu").remove()
	});
	var k = layui.data(d.tableName).theme;
	k ? "admin" == k || layui.link(d.getThemeDir() + k + ".css", k) : "admin" != d.defaultTheme && layui.link(d.getThemeDir() + d.defaultTheme + ".css", d.defaultTheme);
	top.layui && top.layui.index && top.layui.index.pageTabs && b("body").addClass("tab-open");
	l("admind", d)
});