layui.define(["jquery"], function(h) {
	var g = layui.jquery,
		e = {
			bind: function(c, b) {
				g(c).bind("contextmenu", function(a) {
					e.show(b, a.clientX, a.clientY);
					return !1
				})
			},
			getHtml: function(c, b) {
				for (var a = "", d = 0; d < c.length; d++) {
					var f = c[d];
					f.itemId = "ctxMenu-" + b + d;
					f.subs && 0 < f.subs.length ? (a += '<div class="ctxMenu-item haveMore" lay-id="' + f.itemId + '">', a += "<a>", f.icon && (a += '<i class="' + f.icon + ' ctx-icon"></i>'), a += f.name, a += '<i class="layui-icon layui-icon-right icon-more"></i>', a += "</a>", a += '<div class="ctxMenu-sub" style="display: none;">', a += e.getHtml(f.subs, b + d), a += "</div>") : (a += '<div class="ctxMenu-item" lay-id="' + f.itemId + '">', a += "<a>", f.icon && (a += '<i class="' + f.icon + ' ctx-icon"></i>'), a += f.name, a += "</a>");
					a += "</div>";
					1 == f.hr && (a += "<hr/>")
				}
				return a
			},
			setEvents: function(c) {
				for (var b = 0; b < c.length; b++) {
					var a = c[b];
					if (a.click) g(".ctxMenu").on("click", '[lay-id="' + a.itemId + '"]', a.click);
					a.subs && 0 < a.subs.length && e.setEvents(a.subs)
				}
			},
			remove: function() {
				for (var c = top.window.frames, b = 0; b < c.length; b++) {
					var a = c[b];
					try {
						a.layui.jquery(".ctxMenu").remove()
					} catch (d) {}
				}
				try {
					top.layui.jquery(".ctxMenu").remove()
				} catch (d) {}
			},
			getPageHeight: function() {
				return document.documentElement.clientHeight || document.body.clientHeight
			},
			getPageWidth: function() {
				return document.documentElement.clientWidth || document.body.clientWidth
			},
			show: function(c, b, a) {
				var d = '<div class="ctxMenu" style="' + ("left: " + b + "px; top: " + a + "px;") + '">' + e.getHtml(c, "");
				d += "</div>";
				e.remove();
				g("body").append(d);
				d = g(".ctxMenu");
				b + d.outerWidth() > e.getPageWidth() && (b -= d.outerWidth());
				a + d.outerHeight() > e.getPageHeight() && (a -= d.outerHeight(), 0 > a && (a = 0));
				d.css({
					top: a,
					left: b
				});
				e.setEvents(c);
				g(".ctxMenu-item.haveMore").on("mouseenter", function() {
					var a = g(this).find(">a"),
						c = g(this).find(">.ctxMenu-sub"),
						b = a.offset().top,
						d = a.offset().left + a.outerWidth();
					d + c.outerWidth() > e.getPageWidth() && (d = a.offset().left - c.outerWidth());
					b + c.outerHeight() > e.getPageHeight() && (b = b - c.outerHeight() + a.outerHeight(), 0 > b && (b = 0));
					g(this).find(">.ctxMenu-sub").css({
						top: b,
						left: d,
						display: "block"
					})
				}).on("mouseleave", function() {
					g(this).find(">.ctxMenu-sub").css("display", "none")
				})
			}
		};
	g(document).off("click.ctxMenu").on("click.ctxMenu", function() {
		e.remove()
	});
	g("body").off("click.ctxMenuMore").on("click.ctxMenuMore", ".ctxMenu-item", function(c) {
		g(this).hasClass("haveMore") ? void 0 !== c && (c.preventDefault(), c.stopPropagation()) : e.remove()
	});
	h("contextMenud", e)
});