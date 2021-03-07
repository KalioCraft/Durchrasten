package de.kalio.Durchrasten.utils.nms;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NMSManager {
   public static final Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap();

   public static Class<?> getPrimitiveType(Class<?> Class) {
      return CORRESPONDING_TYPES.containsKey(Class) ? (Class)CORRESPONDING_TYPES.get(Class) : Class;
   }

   public static Class<?>[] toPrimitiveTypeArray(Class<?>[] Classes) {
      int L = Classes != null ? Classes.length : 0;
      Class[] T = new Class[L];

      for(int i = 0; i < L; ++i) {
         T[i] = getPrimitiveType(Classes[i]);
      }

      return T;
   }

   public static boolean equalsTypeArray(Class<?>[] Value1, Class<?>[] Value2) {
      if (Value1.length != Value2.length) {
         return false;
      } else {
         for(int i = 0; i < Value1.length; ++i) {
            if (!Value1[i].equals(Value2[i]) && !Value1[i].isAssignableFrom(Value2[i])) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean classListEqual(Class<?>[] Value1, Class<?>[] Value2) {
      if (Value1.length != Value2.length) {
         return false;
      } else {
         for(int i = 0; i < Value1.length; ++i) {
            if (Value1[i] != Value2[i]) {
               return false;
            }
         }

         return true;
      }
   }

   public static String getVersion() {
      String V = Bukkit.getServer().getClass().getPackage().getName();
      return V.substring(V.lastIndexOf(46) + 1) + ".";
   }

   public static boolean useNewVersion() {
      try {
         Class.forName("net.minecraft.server." + getVersion() + "ContainerAccess");
         return true;
      } catch (Exception var1) {
         return false;
      }
   }

   public static Field getField(Class<?> Class, String Field) {
      try {
         Field F = Class.getDeclaredField(Field);
         F.setAccessible(true);
         return F;
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static Class<?> getNMSClass(String ClassName) {
      Object C = null;

      try {
         return Class.forName("net.minecraft.server." + getVersion() + ClassName);
      } catch (Exception var3) {
         var3.printStackTrace();
         return (Class)C;
      }
   }

   public static Method getMethod(Class<?> Class, String ClassName, Class<?>... Parameters) {
      Method[] var6;
      int var5 = (var6 = Class.getMethods()).length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Method M = var6[var4];
         if (M.getName().equals(ClassName) && (Parameters.length == 0 || classListEqual(Parameters, M.getParameterTypes()))) {
            M.setAccessible(true);
            return M;
         }
      }

      return null;
   }

   public static Method getMethod(String MethodName, Class<?> Class, Class<?>... Parameters) {
      Class[] T = toPrimitiveTypeArray(Parameters);
      Method[] var7;
      int var6 = (var7 = Class.getMethods()).length;

      for(int var5 = 0; var5 < var6; ++var5) {
         Method M = var7[var5];
         if (M.getName().equals(MethodName) && equalsTypeArray(toPrimitiveTypeArray(M.getParameterTypes()), T)) {
            return M;
         }
      }

      return null;
   }

   public static Object getHandle(Object Object) {
      try {
         return getMethod("getHandle", Object.getClass()).invoke(Object);
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static Object getPlayerField(Player Player, String Field) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
      Object P = Player.getClass().getMethod("getHandle").invoke(Player);
      return P.getClass().getField(Field).get(P);
   }

   public static Object invokeMethod(String MethodName, Object Parameter) {
      try {
         return getMethod(MethodName, Parameter.getClass()).invoke(Parameter);
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static Object invokeMethodWithArgs(String MethodName, Object Object, Object... Parameters) {
      try {
         return getMethod(MethodName, Object.getClass()).invoke(Object, Parameters);
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public static boolean set(Object Object, String Field, Object Value) {
      Class C = Object.getClass();

      while(C != null) {
         try {
            Field F = C.getDeclaredField(Field);
            F.setAccessible(true);
            F.set(Object, Value);
            return true;
         } catch (NoSuchFieldException var5) {
            C = C.getSuperclass();
         } catch (Exception var6) {
            throw new IllegalStateException(var6);
         }
      }

      return false;
   }
}