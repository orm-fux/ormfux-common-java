package org.ormfux.common.utils.reflection.testclassutils.mock;


public enum ComplexEnum {
    
    VALUE1("value1") {
        
        @Override
        public void method() {
            System.out.println(property);
        }
    },
    
    VALUE2("value2") {
        
        @Override
        public void method() {
            System.out.println(property);
        }
    };
    
    protected final String property;
    
    
    private ComplexEnum(String property) {
        this.property = property;
    }
    
    public abstract void method();
    
}
