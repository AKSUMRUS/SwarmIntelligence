package com.aksum.swarmintelligence;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.Console;

import jdk.internal.net.http.common.Log;

public class Main extends ApplicationAdapter {
	public static final int SCR_WIDTH = 1800, SCR_HEIGHT = 1000; // screen size
	SpriteBatch batch;
	OrthographicCamera camera; // камера для масштабирования под все разрешения экранов

	// textures and sound
	Texture imgBoid;
	Texture imgQueen;
	Texture imgBlueResource;
	Texture imgRedResource;
	Texture imgGreenResource;

	Array<Boid> boids = new Array<>();
	Array<Queen> queens = new Array<>();
	Array<Scout> scouts = new Array<>();
	Array<Resource> blueResource = new Array<>();

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);

		// load resources and sounds
		loadResources();

		// create boids objects
		for(int i = 0;i < 1000;i++) {
			boids.add(new Boid());
		}

		// create queens objects
		for(int i = 0;i < 3;i++) {
			queens.add(new Queen());
		}

		// create scouts objects
		for(int i = 0;i < 0;i++) {
			scouts.add(new Scout());
		}

		// create blue resource objects
		for(int i = 0;i < 4;i++) {
			blueResource.add(new Resource());
		}
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();

		// move boids
		for(int i = 0;i < boids.size;i++){
			boids.get(i).move();
		}

		// move queens
		for(int i = 0;i < queens.size;i++){
			queens.get(i).move();
		}

		// move scouts
		for(int i = 0;i < scouts.size;i++){
			scouts.get(i).move();
		}

		// move blue resources
		for(int i = 0;i < blueResource.size;i++){
			blueResource.get(i).move();
		}

		// Проверяем достигли ли боиды цели
		for(int i = 0;i < boids.size;i++){
			for(int j = 0;j < queens.size;j++){
				if(boids.get(i).overlaps(queens.get(j))){
					Boid b = boids.get(i);
					b.distanceA = 0;
					if(b.isNeedA){
						b.isNeedA = false;
						b.isNeedB = true;
						b.a = b.a + 180;
					}
					boids.set(i,b);
					break;
				}
			}
			for(int j = 0;j < blueResource.size;j++){
				if(boids.get(i).overlaps(blueResource.get(j))){
					Boid b = boids.get(i);
					b.distanceB = 0;
					if(b.isNeedB){
						b.isNeedB = false;
						b.isNeedA = true;
						b.a = b.a + 180;
					}
					boids.set(i,b);
					break;
				}
			}
		}

		// Проверяем достигли ли скауты кого-нибудь
		for(int i = 0;i < scouts.size;i++){
			for(int j = 0;j < queens.size;j++){
				if(scouts.get(i).overlaps(queens.get(j))){
					Scout b = scouts.get(i);
					b.distanceA = 0;
					scouts.set(i,b);
					break;
				}
			}
			for(int j = 0;j < blueResource.size;j++){
				if(scouts.get(i).overlaps(blueResource.get(j))){
					Scout b = scouts.get(i);
					b.distanceB = 0;
					scouts.set(i,b);
					break;
				}
			}
		}

		// Передаем данные между скаутами
		for(int i = 0;i < scouts.size;i++){
			for(int j = 0;j < scouts.size;j++){
				if(i == j) continue;
				Scout b = scouts.get(i);
				Scout a = scouts.get(j);
				if(a.distanceA + a.radius < b.distanceA){
					b.distanceA = a.distanceA + (int)a.radius;
				}
				if(a.distanceB + a.radius < b.distanceB){
					b.distanceB = a.distanceB + (int)a.radius;
				}
				scouts.set(i,b);
			}
		}

		// Боиды принимают данные от скаутов
		for(int i = 0;i < scouts.size;i++){
			for(int j = 0;j < boids.size;j++){
				if(i == j) continue;
				Scout a = scouts.get(i);
				Boid b = boids.get(j);
				boolean isUpdated = false;
				if (a.distanceA + a.radius < b.distanceA) {
					b.distanceA = a.distanceA + (int)a.radius;
					isUpdated = true;
				}
				if (a.distanceB + a.radius < b.distanceB) {
					b.distanceB = a.distanceB + (int)a.radius;
					isUpdated = true;
				}
				if(isUpdated) {
					boids.set(j, b);
					updatePos(j);
				}
			}
		}

		// Передаем данные между боидами
		for(int i = 0;i < boids.size;i++){
			updatePos(i);
		}

		for(int i = 0;i < boids.size;i++){
			batch.draw(imgBoid,boids.get(i).x - boids.get(i).innerWidth/2,boids.get(i).y - boids.get(i).innerHeight/2,boids.get(i).innerWidth,boids.get(i).innerHeight);
		}

		for(int i = 0;i < queens.size;i++){
			batch.draw(imgQueen,queens.get(i).x - queens.get(i).width/2,queens.get(i).y - queens.get(i).height/2,queens.get(i).width,queens.get(i).height);
		}

		for(int i = 0;i < scouts.size;i++){
			batch.draw(imgBoid,scouts.get(i).x - scouts.get(i).innerWidth/2,scouts.get(i).y - scouts.get(i).innerHeight/2,scouts.get(i).innerWidth,scouts.get(i).innerHeight);
		}

		for(int i = 0;i < blueResource.size;i++){
			batch.draw(imgBlueResource,blueResource.get(i).x - blueResource.get(i).width/2,blueResource.get(i).y - blueResource.get(i).height/2,blueResource.get(i).width,blueResource.get(i).height);
		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	// load textures and sounds
	public void loadResources() {
		// load textures
		imgBoid = new Texture("boid.png");
		imgQueen = new Texture("queen.png");
		imgBlueResource = new Texture("blue_resource.png");
		imgRedResource = new Texture("red_resource.png");
		imgGreenResource = new Texture("green_resource.png");
	}

	public void updatePos(int i){
		for(int j = 0;j < boids.size;j++) {
			if (i == j) continue;
			if (boids.get(i).overlapsZone(boids.get(j))) {
				Boid a = boids.get(j);
				Boid b = boids.get(i);
				boolean isUpdated = false;
				if (a.distanceA + a.radius < b.distanceA) {
					b.distanceA = a.distanceA + (int)a.radius;
					if(b.isNeedA) {
						float lenX = (a.x - b.x) * (a.x - b.x);
						float lenY = (a.y - b.y) * (a.y - b.y);
						float A = (float) Math.toDegrees(Math.atan(lenY / lenX));
						if (b.x > a.x) {
							A = 180 - A;
						}
						if (b.y > a.y) {
							A = -A;
						}
						b.a = A;
					}
					isUpdated = true;
				}
				if (a.distanceB + a.radius < b.distanceB) {
					b.distanceB = a.distanceB + (int)a.radius;
					if(b.isNeedB) {
						float lenX = (a.x - b.x) * (a.x - b.x);
						float lenY = (a.y - b.y) * (a.y - b.y);
						float A = (float) Math.toDegrees(Math.atan(lenY / lenX));
						if (b.x > a.x) {
							A = 180 - A;
						}
						if (b.y > a.y) {
							A = -A;
						}
						b.a = A;
					}
					isUpdated = true;
				}
				if(isUpdated){
					boids.set(i, b);
					updatePos(j);
				}
			}
		}
	}
}
