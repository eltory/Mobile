package com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Option 설정을 하지 않을 경우 NoneSerializable 해야하므로
 * ValidationAll 에서 따로 빼서 관리
 * 효율적인 옵션 선택을 위해 Builder 를 이용한 객체 생성
 *
 * @author LeeSungHwi
 * @version 1.1 2018.7.16.
 */
public class DefinitionOptions implements Serializable {

    private JsonObject graphic = null;
    private String adjacent = null;
    private JsonObject attribute = null;

    public DefinitionOptions() {
    }

    // 빌더패턴을 활용한 검수 옵션 세팅 생성자
    private DefinitionOptions(Builder builder) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.serializeNulls().create();
        JsonElement element;
        try {
            if (builder.SmallArea != null) {
                if (graphic == null)
                    graphic = new JsonObject();
                element = gson.toJsonTree(builder.SmallArea, Graphic.class);
                graphic.add("SmallArea", element);
            }
            if (builder.SelfEntity != null) {
                if (graphic == null)
                    graphic = new JsonObject();
                element = gson.toJsonTree(builder.SelfEntity, Graphic.class);
                graphic.add("SelfEntity", element);
            }
            if (builder.EntityDuplicated != null) {
                if (graphic == null)
                    graphic = new JsonObject();
                element = gson.toJsonTree(builder.EntityDuplicated, Graphic.class);
                graphic.add("EntityDuplicated", element);
            }
            if (builder.PointDuplicated != null) {
                if (graphic == null)
                    graphic = new JsonObject();
                element = gson.toJsonTree(builder.PointDuplicated, Graphic.class);
                graphic.add("PointDuplicated", element);
            }
            if (builder.SmallLength != null) {
                if (graphic == null)
                    graphic = new JsonObject();
                element = gson.toJsonTree(builder.SmallLength, Graphic.class);
                graphic.add("SmallLength", element);
            }
            if (builder.ConIntersected != null) {
                if (graphic == null)
                    graphic = new JsonObject();
                element = gson.toJsonTree(builder.ConIntersected, Graphic.class);
                graphic.add("ConIntersected", element);
            }
            if (builder.ConOverDegree != null) {
                if (graphic == null)
                    graphic = new JsonObject();
                element = gson.toJsonTree(builder.ConOverDegree, Graphic.class);
                graphic.add("ConOverDegree", element);
            }
            if (builder.ConBreak != null) {
                if (graphic == null)
                    graphic = new JsonObject();
                element = gson.toJsonTree(builder.ConBreak, Graphic.class);
                graphic.add("ConBreak", element);
            }
            if (builder.ZValueAmbiguous != null) {
                if (attribute == null)
                    attribute = new JsonObject();
                element = gson.toJsonTree(builder.ZValueAmbiguous, Attribute.class);
              attribute.add("ZValueAmbiguous", element);
            }
            if (builder.UseLessPoint != null) {
                if (graphic == null)
                    graphic = new JsonObject();
                element = gson.toJsonTree(builder.UseLessPoint, Graphic.class);
                graphic.add("UseLessPoint", element);
            }
        } catch (JsonIOException e) {
            e.printStackTrace();
        }
    }

    public static Object eval(String pkg, String className) throws Exception {
        try {
            Class clazz = Class.forName(pkg + "." + className);
            return clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw e;
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // 빌더패턴을 활용해 각 검수 객체마다 효율적으로 옵션 처리
    public static class Builder implements Buildable {

        private Graphic SmallArea = null;
        private Graphic SelfEntity = null;
        private Graphic EntityDuplicated = null;
        private Graphic PointDuplicated = null;
        private Graphic SmallLength = null;
        private Graphic ConIntersected = null;
        private Graphic ConOverDegree = null;
        private Graphic ConBreak = null;
        private Attribute ZValueAmbiguous = null;
        private Graphic UseLessPoint = null;

        public Builder() {
        }

        public Builder setSmallArea(boolean checked, Tolerance tolerance) {
            if (checked) {
                this.SmallArea = new Graphic();
                this.SmallArea.setTolerance(tolerance);
            }
            return this;
        }

        public Builder setSelfEntity(boolean checked) {
            if (checked) this.SelfEntity = new Graphic();
            return this;
        }

        public Builder setEntityDuplicated(boolean checked) {
            if (checked) this.EntityDuplicated = new Graphic();
            return this;
        }

        public Builder setPointDuplicated(boolean checked) {
            if (checked) this.PointDuplicated = new Graphic();
            return this;
        }

        public Builder setSmallLength(boolean checked, Tolerance tolerance) {
            if (checked) {
                this.SmallLength = new Graphic();
                this.SmallLength.setTolerance(tolerance);
            }
            return this;
        }

        public Builder setConIntersected(boolean checked) {
            if (checked) this.ConIntersected = new Graphic();
            return this;
        }

        public Builder setConOverDegree(boolean checked, Tolerance tolerance) {
            if (checked) {
                this.ConOverDegree = new Graphic();
                this.ConOverDegree.setTolerance(tolerance);
            }
            return this;
        }

        public Builder setConBreak(boolean checked) {
            if (checked) this.ConBreak = new Graphic();
            return this;
        }

        public Builder setZValueAmbiguous(boolean checked, Figure figure) {
            if (checked) {
                this.ZValueAmbiguous = new Attribute();
                this.ZValueAmbiguous.setFigure(figure);
            }
            return this;
        }

        public Builder setUseLessPoint(boolean checked) {
            if (checked) this.UseLessPoint = new Graphic();
            return this;
        }

        public DefinitionOptions build() {
            return new DefinitionOptions(this);
        }
    }

    public void setAdjacent(String adjacent) {
        this.adjacent = adjacent;
    }

    public String getAdjacent() {
        return adjacent;
    }

}
