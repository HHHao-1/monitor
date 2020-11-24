package com.chaindigg.monitor.common.utils;

import com.chaindigg.monitor.common.enums.NoticeWay;

public class SearchNoticeWay {
  public static Integer noticeWayId(String noticeWay) {
    if (noticeWay.equals(NoticeWay.APP.message)) {
      return NoticeWay.APP.code;
    } else if (noticeWay.equals(NoticeWay.SMS.message)) {
      return NoticeWay.SMS.code;
    } else if (noticeWay.equals(NoticeWay.EMAIL.message)) {
      return NoticeWay.EMAIL.code;
    } else {
      return NoticeWay.CLIENT.code;
    }
  }
}
