package com.atisz.appface;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 *
 * @author wuwei
 * @date 2019/2/25
 */

public class AppContextLike extends TinkerApplication {
    public AppContextLike() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.atisz.appface.AppContext",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
