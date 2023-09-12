package com.github.agentallandev.jade;

public class LevelScene extends Scene{

    public LevelScene(){
        System.out.println("Level Editor Scene Init");
        Window.get().r = 1;
        Window.get().g = 1;
        Window.get().b = 1;
    }

    @Override
    public void update(float dt) {
    }
}
