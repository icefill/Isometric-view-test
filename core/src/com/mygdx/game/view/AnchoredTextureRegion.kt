package com.mygdx.game.view

import com.badlogic.gdx.graphics.g2d.TextureRegion

class AnchoredTextureRegion : TextureRegion {
    internal var anchor_x: Float = 0f
    internal var anchor_y: Float = 0f
    internal var offset_x:Float
    internal var offset_y:Float
    internal var modifier: Float = 0f
    constructor(anchor_x: Float, anchor_y: Float,offset_x:Float,offset_y:Float, region: TextureRegion) : super(region) {
        this.anchor_x = anchor_x
        this.anchor_y = anchor_y
        this.offset_x=offset_x
        this.offset_y=offset_y

    }
}