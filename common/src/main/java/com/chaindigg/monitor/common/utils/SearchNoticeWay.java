package com.chaindigg.monitor.common.utils;

import com.chaindigg.monitor.common.enums.NoticeWay;

public class SearchNoticeWay {
  public static Integer noticeWayId(String noticeWay) {
    if (noticeWay.equals(NoticeWay.SMS.message)) {
      return NoticeWay.SMS.code;
    }
    if (noticeWay.equals(NoticeWay.EMAIL.message)) {
      return NoticeWay.EMAIL.code;
    }
    if (noticeWay.equals(NoticeWay.CLIENT.message)) {
      return NoticeWay.EMAIL.code;
    }
    if (noticeWay.equals(NoticeWay.SMS_AND_EMAIL.message)) {
      return NoticeWay.EMAIL.code;
    }
    if (noticeWay.equals(NoticeWay.SMS_AND_CLIENT.message)) {
      return NoticeWay.EMAIL.code;
    }
    if (noticeWay.equals(NoticeWay.EMAIL_AND_CLIENT.message)) {
      return NoticeWay.EMAIL.code;
    }
    if (noticeWay.equals(NoticeWay.SMS_EMAIL_CLIENT.message)) {
      return NoticeWay.EMAIL.code;
    } else {
      return NoticeWay.Exception_WAY.code;
    }
  }
}
