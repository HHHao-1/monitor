package com.chaindigg.monitor.utils;

public class SearchNoticeWay {
  public static Integer noticeWayId (String noticeWay){
    if (noticeWay.equals(com.chaindigg.monitor.enums.NoticeWay.APP.message)){
      return com.chaindigg.monitor.enums.NoticeWay.APP.code;
    }else if (noticeWay.equals(com.chaindigg.monitor.enums.NoticeWay.SMS.message)){
      return com.chaindigg.monitor.enums.NoticeWay.SMS.code;
    }else if (noticeWay.equals(com.chaindigg.monitor.enums.NoticeWay.EMAIL.message)){
      return com.chaindigg.monitor.enums.NoticeWay.EMAIL.code;
    }else {
      return com.chaindigg.monitor.enums.NoticeWay.CLIENT.code;
    }
  }
}
