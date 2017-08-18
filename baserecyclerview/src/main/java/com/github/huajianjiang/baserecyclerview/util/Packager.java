package com.github.huajianjiang.baserecyclerview.util;

/**
 * 本地 ItemViewType(parent、child)和客户端返回的多种类型的 parent 或/和 child ItemViewType 打包器
 * <p>#方便在 方法中返回本地 parent 和 child 与客户端可能返回的多种类型的 parent 或/和
 * child 类型的打包后的
 * ItemViewType
 * ，在 方法中将传递过来的先前打包过的
 * ItemViewType 解包为具体的本地类型和客户端返回的类型(parent、child)来进行判断和回调</p>
 */
public class Packager {
    private static final String TAG = "Packager";
    private static final int TYPE_SHIFT = 31;
    private static final int TYPE_MASK = 0x1 << TYPE_SHIFT;

    public static final int ITEM_VIEW_TYPE_HEADER = 0;
    public static final int ITEM_VIEW_TYPE_BODY = 1 << TYPE_SHIFT;

    //------------打包(客户端的 ItemView 类型和本地类型)----------------
    public static int makeItemViewTypeSpec(int clientViewType, int localViewType) {
        return (clientViewType & ~TYPE_MASK) | (localViewType & TYPE_MASK);
    }

    //解包(本地ItemViewType)
    public static int getLocalViewType(int itemViewTypeSpec) {
        return (itemViewTypeSpec & TYPE_MASK);
    }

    //解包(客户端ItemViewType)
    public static int getClientViewType(int itemViewTypeSpec) {
        return (itemViewTypeSpec & ~TYPE_MASK);
    }
}
