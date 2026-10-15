import os

RES = os.path.join(os.path.dirname(os.path.abspath(__file__)),
                   "android", "app", "src", "main", "res")

HDR = '<?xml version="1.0" encoding="utf-8"?>\n'
NS = 'xmlns:android="http://schemas.android.com/apk/res/android"'

FILES = {}


def shape(name, body, kind="rectangle"):
    FILES["drawable/%s.xml" % name] = (
        HDR + '<shape %s android:shape="%s">\n%s</shape>\n' % (NS, kind, body))


def raw(path, content):
    FILES[path] = HDR + content


shape("bg_splash_gradient", """    <gradient
        android:type="linear"
        android:angle="270"
        android:startColor="@color/herb_green_forest"
        android:centerColor="@color/herb_green_deep"
        android:endColor="@color/herb_green_night" />
""")

raw("drawable/launch_background.xml", """<layer-list %s>
    <item android:drawable="@drawable/bg_splash_gradient" />
    <item
        android:gravity="center"
        android:width="160dp"
        android:height="160dp"
        android:drawable="@drawable/sprite_press_book" />
</layer-list>
""" % NS)

shape("scrim_splash", """    <gradient
        android:type="linear"
        android:angle="270"
        android:startColor="@color/herb_scrim_none"
        android:centerColor="@color/herb_scrim_none"
        android:endColor="@color/herb_scrim_strong" />
""")

shape("wash_paper", """    <gradient
        android:type="linear"
        android:angle="270"
        android:startColor="@color/herb_wash_top"
        android:endColor="@color/herb_wash_bottom" />
""")

shape("wash_paper_strong", """    <gradient
        android:type="linear"
        android:angle="270"
        android:startColor="@color/herb_wash_strong_top"
        android:endColor="@color/herb_wash_strong_bottom" />
""")

raw("drawable/surface_header.xml", """<layer-list %s>
    <item>
        <shape android:shape="rectangle">
            <solid android:color="@color/herb_header_surface" />
        </shape>
    </item>
    <item android:gravity="bottom">
        <shape android:shape="rectangle">
            <size android:height="1dp" />
            <solid android:color="@color/herb_hairline" />
        </shape>
    </item>
</layer-list>
""" % NS)

shape("surface_panel", """    <solid android:color="@color/herb_panel_surface" />
    <corners
        android:topLeftRadius="@dimen/panel_radius"
        android:topRightRadius="@dimen/panel_radius" />
    <stroke android:width="@dimen/hairline" android:color="@color/herb_hairline" />
""")

shape("plate_grid", """    <solid android:color="@android:color/transparent" />
    <corners android:radius="@dimen/card_radius_small" />
    <stroke android:width="@dimen/hairline" android:color="@color/herb_plate_stroke" />
""")

shape("outline_dashed_paper", """    <solid android:color="@color/herb_paper_card" />
    <corners android:radius="@dimen/card_radius_small" />
    <stroke
        android:width="@dimen/hairline"
        android:color="@color/herb_green_primary"
        android:dashWidth="6dp"
        android:dashGap="4dp" />
""")

shape("skeleton_paper", """    <solid android:color="@color/herb_skeleton" />
    <corners android:radius="@dimen/card_radius_small" />
""")

shape("banner_warning", """    <solid android:color="@color/herb_paper_card" />
    <corners android:radius="@dimen/block_gap" />
    <stroke android:width="@dimen/hairline" android:color="@color/herb_terracotta" />
""")

shape("surface_paper_card", """    <solid android:color="@color/herb_paper_card" />
    <corners android:radius="@dimen/plate_radius" />
    <stroke android:width="@dimen/hairline" android:color="@color/herb_hairline" />
""")

shape("step_pill", """    <solid android:color="@color/herb_green_soft" />
    <corners android:radius="12dp" />
""")

shape("count_chip", """    <solid android:color="@color/herb_paper_tint" />
    <corners android:radius="10dp" />
    <stroke android:width="@dimen/hairline" android:color="@color/herb_hairline" />
""")

shape("marker_ribbon", """    <solid android:color="@color/herb_marker" />
    <corners android:radius="2dp" />
""")

shape("dot_marker", """    <solid android:color="@color/herb_marker" />
""", kind="oval")

shape("dot_accent", """    <solid android:color="@color/herb_green_primary" />
""", kind="oval")

for nm, col in (("sage", "herb_swatch_sage"), ("deep", "herb_swatch_deep"),
                ("pale", "herb_swatch_pale"), ("yellow", "herb_swatch_yellow"),
                ("rust", "herb_swatch_rust")):
    shape("swatch_%s" % nm, """    <solid android:color="@color/%s" />
    <stroke android:width="@dimen/hairline" android:color="@color/herb_hairline" />
""" % col, kind="oval")

raw("drawable/swatch_ring.xml", """<selector %s>
    <item android:state_selected="true">
        <shape android:shape="oval">
            <solid android:color="@android:color/transparent" />
            <stroke android:width="2dp" android:color="@color/herb_green_primary" />
        </shape>
    </item>
    <item>
        <shape android:shape="oval">
            <solid android:color="@android:color/transparent" />
            <stroke android:width="@dimen/hairline" android:color="@android:color/transparent" />
        </shape>
    </item>
</selector>
""" % NS)

raw("drawable/chip_trait_selector.xml", """<selector %s>
    <item android:state_selected="true">
        <shape android:shape="rectangle">
            <solid android:color="@color/herb_paper_card" />
            <corners android:radius="@dimen/card_radius_small" />
            <stroke android:width="2dp" android:color="@color/herb_green_primary" />
        </shape>
    </item>
    <item>
        <shape android:shape="rectangle">
            <solid android:color="@color/herb_paper_card" />
            <corners android:radius="@dimen/card_radius_small" />
            <stroke android:width="@dimen/hairline" android:color="@color/herb_hairline" />
        </shape>
    </item>
</selector>
""" % NS)

raw("drawable/ripple_primary.xml", """<ripple %s android:color="@color/herb_ripple_marker">
    <item android:drawable="@drawable/chip_trait_selector" />
    <item android:id="@android:id/mask">
        <shape android:shape="rectangle">
            <solid android:color="#FFFFFFFF" />
            <corners android:radius="@dimen/card_radius_small" />
        </shape>
    </item>
</ripple>
""" % NS)

raw("drawable/ripple_secondary.xml", """<ripple %s android:color="@color/herb_ripple_marker">
    <item android:drawable="@drawable/swatch_ring" />
    <item android:id="@android:id/mask">
        <shape android:shape="oval">
            <solid android:color="#FFFFFFFF" />
        </shape>
    </item>
</ripple>
""" % NS)


def vector(name, path_data, tint="@color/herb_ink_soft", fill_type=None):
    ft = ' android:fillType="%s"' % fill_type if fill_type else ""
    raw("drawable/%s.xml" % name, """<vector %s
    android:width="@dimen/icon_size"
    android:height="@dimen/icon_size"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="%s">
    <path
        android:fillColor="#FFFFFFFF"%s
        android:pathData="%s" />
</vector>
""" % (NS, tint, ft, path_data))


vector("ic_back", "M20,11H7.8l5.6,-5.6L12,4l-8,8 8,8 1.4,-1.4L7.8,13H20z")
vector("ic_leaf_add", "M11,5h2v6h6v2h-6v6h-2v-6H5v-2h6z")
vector("ic_press", "M11,3h2v8h4l-5,5 -5,-5h4z M4,19h16v2H4z")
vector("ic_settings",
       "M3,7h7v2H3z M14,7h7v2h-7z M10,4h4v8h-4z "
       "M3,15h3v2H3z M10,15h11v2H10z M6,12h4v8H6z",
       fill_type="evenOdd")
vector("ic_watering",
       "M12,3c0,0 6,6.5 6,10.5 0,3.3 -2.7,6 -6,6s-6,-2.7 -6,-6C6,9.5 12,3 12,3z")
vector("ic_collection", "M4,5h16v2H4z M4,11h16v2H4z M4,17h16v2H4z")
vector("ic_warning", "M12,3 22,20H2z M11,10h2v5h-2z M11,16h2v2h-2z",
       tint="@color/herb_terracotta", fill_type="evenOdd")
vector("ic_check", "M9,16.2 4.8,12l-1.4,1.4L9,19 21,7l-1.4,-1.4z",
       tint="@color/herb_green_primary")

raw("anim/fade_in.xml", """<alpha %s
    android:duration="280"
    android:fromAlpha="0.0"
    android:toAlpha="1.0"
    android:interpolator="@android:anim/decelerate_interpolator" />
""" % NS)

raw("anim/fade_out.xml", """<alpha %s
    android:duration="240"
    android:fromAlpha="1.0"
    android:toAlpha="0.0"
    android:interpolator="@android:anim/accelerate_interpolator" />
""" % NS)

raw("anim/slide_in_right.xml", """<set %s
    android:interpolator="@android:anim/decelerate_interpolator">
    <translate
        android:duration="300"
        android:fromXDelta="100%%p"
        android:toXDelta="0%%p" />
    <alpha
        android:duration="300"
        android:fromAlpha="0.4"
        android:toAlpha="1.0" />
</set>
""" % NS)

raw("anim/slide_out_left.xml", """<set %s
    android:interpolator="@android:anim/accelerate_interpolator">
    <translate
        android:duration="280"
        android:fromXDelta="0%%p"
        android:toXDelta="-25%%p" />
    <alpha
        android:duration="280"
        android:fromAlpha="1.0"
        android:toAlpha="0.0" />
</set>
""" % NS)

raw("anim/slide_in_left.xml", """<set %s
    android:interpolator="@android:anim/decelerate_interpolator">
    <translate
        android:duration="300"
        android:fromXDelta="-25%%p"
        android:toXDelta="0%%p" />
    <alpha
        android:duration="300"
        android:fromAlpha="0.4"
        android:toAlpha="1.0" />
</set>
""" % NS)

raw("anim/slide_out_right.xml", """<set %s
    android:interpolator="@android:anim/accelerate_interpolator">
    <translate
        android:duration="280"
        android:fromXDelta="0%%p"
        android:toXDelta="100%%p" />
    <alpha
        android:duration="280"
        android:fromAlpha="1.0"
        android:toAlpha="0.0" />
</set>
""" % NS)

for rel, content in FILES.items():
    target = os.path.join(RES, rel.replace("/", os.sep))
    folder = os.path.dirname(target)
    if not os.path.isdir(folder):
        os.makedirs(folder)
    with open(target, "w", encoding="utf-8") as handle:
        handle.write(content)

print("wrote %d files" % len(FILES))
