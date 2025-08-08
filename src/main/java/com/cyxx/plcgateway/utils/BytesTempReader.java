package com.cyxx.plcgateway.utils;

import com.cyxx.plcgateway.model.entry.IotTemplateEntry;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

@Slf4j
public class BytesTempReader {
    public static byte[] convertShortsToBytes(short[] rawData) {
        byte[] bytes = new byte[rawData.length * 2];
        for (int i = 0; i < rawData.length; i++) {
            bytes[i * 2] = (byte) ((rawData[i] >> 8) & 0xFF);
            bytes[i * 2 + 1] = (byte) (rawData[i] & 0xFF);
        }
        return bytes;
    }

    public Map<String, Object> parseRegistersToMap(byte[] buffer, List<IotTemplateEntry> registerList) {
        Map<String, Object> result = new LinkedHashMap<>();  // 保证输出顺序
        for (IotTemplateEntry item : registerList) {
            int byteLen = item.getLength() * 2;
            int byteOffset = item.getOffset() * 2;
            //判断长度是否足够
            if (byteOffset + byteLen > buffer.length){
                log.error("解析 {} 时超出 buffer 长度: offset={}, length={}, buffer.length={}",item.getFieldName(), byteOffset, byteLen, buffer.length);
                continue;
            }
            byte[] regBytes = Arrays.copyOfRange(buffer, byteOffset, byteOffset + byteLen);
            double val = 0.00;
            if(byteLen == 4){
                val = parseRegister(regBytes, item.getType(), item.getWordSwap(),item.getByteSwap());
            }else{
                val = parseRegister2(regBytes, item.getType());
            }

            val *= item.getScale();
            result.put(item.getFieldName(), (float) val);

        }
        return result;
    }

    public Map<String, Object> parseRegistersToMapS7(byte[] buffer, List<IotTemplateEntry> registerList) {
        Map<String, Object> result = new LinkedHashMap<>();  // 保证输出顺序
        for (IotTemplateEntry item : registerList) {
            int byteOffset = item.getOffset();
            // 2字节，高位在前（S7默认大端）
            if("int".equals(item.getType())){
                int value = ByteBuffer.wrap(buffer, byteOffset, 2).order(ByteOrder.BIG_ENDIAN).getShort() & 0xFFFF;
                result.put(item.getFieldName(), value * item.getScale());
            }else if("Dint".equals(item.getType())){
                long value = ByteBuffer.wrap(buffer, byteOffset, 4).order(ByteOrder.BIG_ENDIAN).getInt() & 0xFFFFFFFFL;
                result.put(item.getFieldName(), value * item.getScale());
            }else if("Real".equals(item.getType())){
                float value = ByteBuffer.wrap(buffer, byteOffset, 4)
                        .order(ByteOrder.BIG_ENDIAN)
                        .getFloat();
                result.put(item.getFieldName(), value * item.getScale());
            }
            else{
                log.error("{}:{}模板类型不存在",item.getFieldName(),item.getType());
            }
        }
        return result;
    }




    public double parseRegister(byte[] rawBytes, String dataType, Integer wordSwap, Integer byteSwap) {
        if (rawBytes == null || rawBytes.length != 4) {
            throw new IllegalArgumentException("Expecting 4 bytes for float/int32, got: " + Arrays.toString(rawBytes));
        }

        byte[] result = rawBytes.clone();

        if (wordSwap != null && wordSwap == 1) {
            result = new byte[]{ result[2], result[3], result[0], result[1] };
        }

        if (byteSwap != null && byteSwap == 1) {
            result = new byte[]{ result[1], result[0], result[3], result[2] };
        }

        System.out.println("RawBytes: " + bytesToHex(result));

        if ("float".equalsIgnoreCase(dataType)) {
            // 用位模式保证一致
            int bits = ByteBuffer.wrap(result).order(ByteOrder.BIG_ENDIAN).getInt();
            float f = Float.intBitsToFloat(bits);
            System.out.println("Bits: " + bits + " -> " + f);
            return f;
        } else if ("int32".equalsIgnoreCase(dataType)) {
            return ByteBuffer.wrap(result).order(ByteOrder.BIG_ENDIAN).getInt();
        } else {
            throw new IllegalArgumentException("Unsupported type: " + dataType);
        }
    }

    public double parseRegister2(byte[] rawBytes, String dataType) {
        if (rawBytes == null || rawBytes.length != 2) {
            throw new IllegalArgumentException("Expecting 2 bytes for int16/uint16");
        }
        byte[] result = rawBytes.clone();
        if ("uint16".equalsIgnoreCase(dataType)) {
            return (result[0] & 0xFF) << 8 | (result[1] & 0xFF);
        } else if ("int16".equalsIgnoreCase(dataType) || "short".equalsIgnoreCase(dataType)) {
            return ByteBuffer.wrap(result).order(ByteOrder.BIG_ENDIAN).getShort();
        } else {
            throw new IllegalArgumentException("Unsupported 2-byte type: " + dataType);
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}
