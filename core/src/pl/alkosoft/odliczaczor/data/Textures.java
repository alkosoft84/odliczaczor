package pl.alkosoft.odliczaczor.data;

public enum Textures {

    BOMB_TEXTURE_ATLAS("gdx/bombs.pack"),
    BOMB_EXPLOSION_TEXTURE_ATLAS("gdx/explodingBombs.pack"),
    EXPLOSION_TEXTURE_ATLAS("gdx/explosion.pack");

    private final String url;

    Textures(String url) {
        this.url = url;
    }

    public String getUrl(){
        return url;
    }
}
