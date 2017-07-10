package game.framework;

public interface IRenderable {
	
	int getVao();
	int getVbo();
	int getEbo();
	int getTexture();
	float[] init(int vao);
}
