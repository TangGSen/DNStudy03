package com.sen.httpservice.http.download.mode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/14.
 * <p>
 * 下面介绍一种全新的方法：利用序列化来做深复制。
 * <p>
 * 　　把对象写到流里的过程是序列化过程（Serialization），而把对象从流中读出来的过程则叫做反序列化过程（Deserialization）。
 * <p>
 * 　　应当指出的是，写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。
 * <p>
 * 　　在Java语言里深复制一个对象，常常可以先使对象实现Serializable接口，然后把对象（实际上只是对象的一个拷贝）写到一个流里，再从流里读出来，便可以重建对象。
 * <p>
 * 　　这样做的前提是对象以及对象内部所有引用到的对象都是可串行化的，否则，就需要仔细考察那些不可串行化的对象可否设成transient，从而将其排除在复制过程之外。
 */

public class BaseEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


    public BaseEntity() {
    }

    public T copy() {
        ObjectOutputStream objectOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(this);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Object result = objectInputStream.readObject();
            inputStream.close();
            objectInputStream.close();
            return (T) result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteArrayOutputStream != null)
                    byteArrayOutputStream.close();

                if (objectOutputStream != null)
                    objectOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return null;
    }
}
