package Chunks;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Cube.Block;
import Cube.Vertex;
import Models.CubeModel;

public class ChunkMesh {
	
	private List<Vertex> vertices;
	
	private List<Float> positionsList;
	private List <Float> uvsList;
	private List <Float> normalsList;
	
	public float[] positions, uvs, normals;
	
	public Chunk chunk;
	
	public ChunkMesh(Chunk chunk) {
		this.chunk = chunk;
		
		vertices = new ArrayList<Vertex>();
		positionsList = new ArrayList<Float>();
		uvsList = new ArrayList<Float>();
		normalsList = new ArrayList<Float>();
		
		buildMesh();
		populateLists();
	}
	
	public void update(Chunk chunk) {
		this.chunk = chunk;
		
		buildMesh();
		populateLists();
		
	}
	
	private void buildMesh() {
		
		//Loops through blocks in chunk and determine which faces need to be shown
		
		for(int i = 0; i < chunk.blocks.size(); i++) {
			
			Block blockI = chunk.blocks.get(i);
			
			boolean px = false, nx = false, py = false, ny = false, pz = false, nz = false;
			
			for(int j = 0; j < chunk.blocks.size(); j++) {
				Block blockJ = chunk.blocks.get(j);
				
				//this checks to see if any of the blocks in the chunk have a position to 1 direction in the x direction and so on
				if(((blockI.x + 1) == (blockJ.x)) && (blockI.z == blockJ.z) && (blockI.y == blockJ.y)) {
					px = true;
				}
				
				if(((blockI.x - 1) == (blockJ.x)) && (blockI.z == blockJ.z) && (blockI.y == blockJ.y)) {
					nx = true;
				}
				
				if(((blockI.x) == (blockJ.x)) && (blockI.z == blockJ.z) && (blockI.y + 1 == blockJ.y)) {
					py = true;
				}
				
				if(((blockI.x) == (blockJ.x)) && (blockI.z == blockJ.z) && (blockI.y - 1== blockJ.y)) {
					ny = true;
				}
				
				if(((blockI.x) == (blockJ.x)) && (blockI.z + 1 == blockJ.z) && (blockI.y == blockJ.y)) {
					pz = true;
				}
				
				if(((blockI.x) == (blockJ.x)) && (blockI.z - 1 == blockJ.z) && (blockI.y == blockJ.y)) nz = true;
					
			}
			
			//Add visible faces to the chunk mesh
			if(!px) {
				for(int k = 0; k < 6; k++) {
					vertices.add(new Vertex(new Vector3f(CubeModel.PX_POS[k].x + blockI.x, CubeModel.PX_POS[k].y + blockI.y, CubeModel.PX_POS[k].z + blockI.z), 
							CubeModel.UV_PX[(blockI.type * 6) + k], CubeModel.NORMALS[k]));
				}
			}
			
			if(!nx) {
				for(int k = 0; k < 6; k++) {
					vertices.add(new Vertex(new Vector3f(CubeModel.NX_POS[k].x + blockI.x, CubeModel.NX_POS[k].y + blockI.y, CubeModel.NX_POS[k].z + blockI.z), 
							CubeModel.UV_NX[(blockI.type * 6) + k], CubeModel.NORMALS[k]));
				}
			}
			
			if(!py) {
				for(int k = 0; k < 6; k++) {
					vertices.add(new Vertex(new Vector3f(CubeModel.PY_POS[k].x + blockI.x, CubeModel.PY_POS[k].y + blockI.y, CubeModel.PY_POS[k].z + blockI.z),
							CubeModel.UV_PY[(blockI.type * 6) + k], CubeModel.NORMALS[k]));
				}
			}
			
			if(!ny) {
				for(int k = 0; k < 6; k++) {
					vertices.add(new Vertex(new Vector3f(CubeModel.NY_POS[k].x + blockI.x, CubeModel.NY_POS[k].y + blockI.y, CubeModel.NY_POS[k].z + blockI.z),
							CubeModel.UV_NY[(blockI.type * 6) + k], CubeModel.NORMALS[k]));
				}
			}
			
			if(!pz) {
				for(int k = 0; k < 6; k++) {
					vertices.add(new Vertex(new Vector3f(CubeModel.PZ_POS[k].x + blockI.x, CubeModel.PZ_POS[k].y + blockI.y, CubeModel.PZ_POS[k].z + blockI.z),
							CubeModel.UV_PZ[(blockI.type * 6) + k], CubeModel.NORMALS[k]));
				}
			}
			
			if(!nz) {
				for(int k = 0; k < 6; k++) {
					vertices.add(new Vertex(new Vector3f(CubeModel.NZ_POS[k].x + blockI.x, CubeModel.NZ_POS[k].y + blockI.y, CubeModel.NZ_POS[k].z + blockI.z), 
							CubeModel.UV_NZ[(blockI.type * 6) + k], CubeModel.NORMALS[k]));
				}
			}
			
			
		}
		
	}
	
	private void populateLists() {
		for(int i = 0; i < vertices.size(); i++) {
			positionsList.add(vertices.get(i).positions.x);
			positionsList.add(vertices.get(i).positions.y);
			positionsList.add(vertices.get(i).positions.z);
			uvsList.add(vertices.get(i).uvs.x);
			uvsList.add(vertices.get(i).uvs.y);
			normalsList.add(vertices.get(i).normals.x);
			normalsList.add(vertices.get(i).normals.y);
			normalsList.add(vertices.get(i).normals.z);
		}
		
		positions = new float[positionsList.size()];
		uvs = new float[uvsList.size()];
		normals = new float[normalsList.size()];
		
		for(int i = 0; i < positionsList.size(); i++) {
			if(positionsList.get(i) != null) {
				positions[i] = positionsList.get(i);
			} else {
				positions[i] = Float.NaN;
				System.err.println("Sent null value to positions float[] in ChunkMesh");
			}
			
		}
		
		for(int i = 0; i < uvsList.size(); i++) {
			if(uvsList.get(i) != null) {
				uvs[i] = uvsList.get(i);
			} else {
				uvs[i] = Float.NaN;
				System.err.println("Sent null value to uvs float[] in ChunkMesh");
			}
			
		}
		
		for(int i = 0; i < normalsList.size(); i++) {
			if(normalsList.get(i) != null) {
				normals[i] = normalsList.get(i);
			} else {
				normals[i] = Float.NaN;
				System.err.println("Sent null value to normals float[] in ChunkMesh");
			}
			
		}
		
		positionsList.clear();
		uvsList.clear();
		normalsList.clear();
		
	}
	
}
