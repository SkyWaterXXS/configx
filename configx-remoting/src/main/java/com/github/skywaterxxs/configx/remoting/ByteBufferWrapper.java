package com.github.skywaterxxs.configx.remoting;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.netting.ByteBufferWrapper</p>
 * <p>描述:  </p>
 * <p>日期: 2018/8/31 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ByteBufferWrapper {

     void writeByte(int index, byte data);

     void writeByte(byte data);

     byte readByte();

     void writeInt(int data);

     void writeBytes(byte[] data);

     int readableBytes();

     int readInt();

     void readBytes(byte[] dst);

     int readerIndex();

     void setReaderIndex(int readerIndex);

     void writeLong(long id);

     long readLong();

     void ensureCapacity(int capacity);
}
