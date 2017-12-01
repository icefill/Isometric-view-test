/*
package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.anim.Anim;

public class AnimLoader extends AsynchronousAssetLoader<Anim, AnimLoader.AnimParameter> {
    public AnimLoader (FileHandleResolver resolver) {
        super(resolver);
    }

    static Json json=new Json();
    Anim anim;

    @Override
    public void loadAsync (AssetManager manager, String fileName, FileHandle file, AnimParameter parameter) {
        anim = null;
        anim= json.fromJson(Anim.class,Gdx.files.local(fileName));
    }

    @Override
    public Anim loadSync (AssetManager manager, String fileName, FileHandle file, AnimParameter parameter) {
        Anim anim = this.anim;
        this.anim = null;
        return anim;
    }

    @Override
    public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, AnimParameter parameter) {
        return null;
    }

    static public class AnimParameter extends AssetLoaderParameters<Anim> {
    }
}*/