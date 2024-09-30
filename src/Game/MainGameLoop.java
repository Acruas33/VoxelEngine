package Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import Chunks.Chunk;
import Chunks.ChunkMesh;
import Cube.Block;
import Entities.Camera;
import Entities.Entity;
import Models.AtlasCubeModel;
import Models.CubeModel;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import Shaders.StaticShader;
import Textures.ModelTexture;
import Toolbox.Maths;
import Toolbox.PerlinNoiseGenerator;

public class MainGameLoop {
	
	public static Loader loader1 = null;
	public static StaticShader shader1 = null;
	
	static List<ChunkMesh> chunks = Collections.synchronizedList(new ArrayList<ChunkMesh>());
	static Vector3f camPos = new Vector3f(0, 0, 0);
	static List<Vector3f> usedPos = new ArrayList<Vector3f>();
	
	static List<Entity> entities = new ArrayList<Entity>();
	
	static final int chunkSize = 32;
	static final int WORLD_SIZE = 5 * chunkSize; //render distance

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		loader1 = loader;
		StaticShader shader = new StaticShader();
		shader1 = shader;
		
		MasterRenderer renderer = new MasterRenderer();
		
		RawModel model = loader.loadToVAO(CubeModel.vertices, CubeModel.indices, CubeModel.uv);
		
		ModelTexture texture = new ModelTexture(loader.loadTexture("textureAtlas1"));
		
		TexturedModel texturedModel = new TexturedModel(model, texture);
		
		Camera camera = new Camera(camPos, 0, 0, 0);
		
		PerlinNoiseGenerator generator = new PerlinNoiseGenerator();
		
		new Thread(new Runnable() {
			public void run() {
				
				while(!Display.isCloseRequested()) {
					
					for(int x = (int) (camPos.x - WORLD_SIZE) / chunkSize; x < (camPos.x + WORLD_SIZE) / chunkSize; x++) {
						for(int z = (int) (camPos.z - WORLD_SIZE) / chunkSize; z < (camPos.z + WORLD_SIZE) / chunkSize; z++) {
							
							Vector3f chunkOrigin = new Vector3f(x * chunkSize, 0, z * chunkSize);
							
							if(!usedPos.contains(chunkOrigin)) {
								
								List<Block> blocks = new ArrayList<Block>();
								
								for(int i = 0; i < chunkSize; i++) {
									for(int j = 0; j < chunkSize; j++) {
										blocks.add(new Block(i, (int) generator.generateHeight(i + (x * chunkSize), j + (z * chunkSize)), j, Block.GRASS));
										
									}
								}
								
								Chunk chunk = new Chunk(blocks, chunkOrigin);
								
								chunks.add(new ChunkMesh(chunk));
								usedPos.add(chunkOrigin);
							}
							
						}
					}
					
				}

			}
		}).start();
		/*
		List<Block> blocks = new ArrayList<Block>();
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				for(int k = 0; k < 10; k++) {
					
					blocks.add(new Block(i, j, k, TYPE.DIRT));
					
				}
			}
		}
		
		Chunk chunk = new Chunk(blocks, new Vector3f(0, 0, 0));
		ChunkMesh chunkMesh = new ChunkMesh(chunk);
		
		RawModel model123 = loader.loadToVAO(chunkMesh.positions, chunkMesh.uvs);
		TexturedModel texModel = new TexturedModel(model123, texture);
		Entity entity = new Entity(texModel, new Vector3f(0, 0, 0), 0, 0, 0, 1);*/
		
		//Main Game Loop
		int index = 0;
		while(!Display.isCloseRequested()) {
			
			camera.move();
			camPos = camera.getPosition();
			
			if(index < chunks.size()) {
				
				RawModel model123 = loader.loadToVAO(chunks.get(index).positions, chunks.get(index).uvs);
				TexturedModel texModel = new TexturedModel(model123, texture);
				Entity entity = new Entity(texModel, chunks.get(index).chunk.origin, 0, 0, 0, 1);
				entities.add(entity);
				
				chunks.get(index).positions = null;
				chunks.get(index).normals = null;
				chunks.get(index).uvs = null;
				
				index++;
			}
			
			
			for(int i = 0; i < entities.size(); i++) {
				
				Vector3f origin = entities.get(i).getPosition();
				//List<Entity> entities = chunks.get(i).getEntities();
				
				int distX = Math.abs((int) (camPos.x - origin.x));
				int distZ = Math.abs((int) (camPos.z - origin.z));
				
				if(distX <= WORLD_SIZE && distZ <= WORLD_SIZE) {
					renderer.addEntity(entities.get(i));
				}
				
				
			}
			
			//renderer.addEntity(entity);
			renderer.render(camera);
			
			DisplayManager.updateDisplay();
			
			
		}
		
		DisplayManager.closeDisplay();
	}

}
