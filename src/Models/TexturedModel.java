package Models;

import Textures.ModelTexture;

public class TexturedModel {

	RawModel model;
	ModelTexture texture;
	//ModelTexture[] textures;

	public TexturedModel(RawModel model, ModelTexture texture) {
		this.model = model;
		this.texture = texture;
	}
	
	public RawModel getRawModel() {
		return model;
	}

	public void setRawModel(RawModel model) {
		this.model = model;
	}

	public ModelTexture getTexture() {
		return texture;
	}

	public void setTexture(ModelTexture texture) {
		this.texture = texture;
	}
	
}
