package com.chaindigg.monitor.admin.dto;

import com.zhifantech.notify.enums.TxTypeEnum;
import com.zhifantech.notify.utils.TimeUtil;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author: shao jianshuang
 * @date: 2018/8/16 下午6:24
 * @description:
 */
public class TxDetail {
    // 币对应的hash
    private String coinHash;

    // 交易类型
    private TxTypeEnum txType;

    // 地址
    private String address;
    // 地址是否是发送地址
    private boolean isSender;
    // 产生的交易量
    private BigDecimal value;
    // 交易hash
    private String txHash;
    // 交易时间
    private long time;
    // 区块高度
    private long blockHeight;

    private String uuid;

    public TxDetail(String coinHash, TxTypeEnum txType, String address, boolean isSender,
                    BigDecimal value, String txHash, long time, long blockHeight, String uuid) {
        this.coinHash = coinHash;
        this.txType = txType;
        this.address = address;
        this.isSender = isSender;
        this.value = value;
        this.txHash = txHash;
        this.time = time;
        this.blockHeight = blockHeight;
        this.uuid = uuid;
    }

    public TxTypeEnum getTxTypeEnum() {
        return txType;
    }

    public void setTxTypeEnum(TxTypeEnum txType) {
        this.txType = txType;
    }

    public String getCoinHash() {
        return coinHash;
    }

    public void setCoinHash(String coinHash) {
        this.coinHash = coinHash;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }

    public boolean isSender() {
        return isSender;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("coin:" + coinHash).append(",")
                .append(" txType:" + TxTypeEnum.getDesc(txType)).append(",")
                .append(" address:" + address).append(",")
                .append(" isSender:" + isSender).append(",")
                .append(" value:"+value).append(",")
                .append(" txHash:"+txHash).append(",")
                .append(" time:"+ TimeUtil.getTimeStr(time)).append(",")
                .append(" blockHeight:"+blockHeight)
                .append(" uuid:"+uuid);

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TxDetail)) {
            return false;
        }
        TxDetail txDetail = (TxDetail) o;
        return isSender == txDetail.isSender &&
                time == txDetail.time &&
                blockHeight == txDetail.blockHeight &&
                Objects.equals(coinHash, txDetail.coinHash) &&
                txType == txDetail.txType &&
                Objects.equals(address, txDetail.address) &&
                Objects.equals(value, txDetail.value) &&
                Objects.equals(uuid,txDetail.uuid)  &&
                Objects.equals(txHash, txDetail.txHash);
    }

    @Override
    public int hashCode() {

        return Objects.hash(coinHash, txType, address, isSender, value, txHash, time, blockHeight,uuid);
    }
}