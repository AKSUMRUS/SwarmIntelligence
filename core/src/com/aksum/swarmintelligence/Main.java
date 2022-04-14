package com.aksum.swarmintelligence;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sun.tools.javac.util.ArrayUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Main extends ApplicationAdapter {
	public static final int SCR_WIDTH = 1800, SCR_HEIGHT = 1000; // screen size
	SpriteBatch batch;
	OrthographicCamera camera; // камера для масштабирования под все разрешения экранов
	Comparator<Boid> comparator;

	// textures and sound
	Texture imgBoid;
	Texture imgBlueBoid;
	Texture imgRedBoid;
	Texture imgGreenBoid;
	Texture imgQueen;
	Texture imgStorageQueen;
	Texture imgBlueResource;
	Texture imgRedResource;
	Texture imgGreenResource;

	Array<Boid> blueBoids = new Array<>();
	Array<Boid> greenBoids = new Array<>();
	Array<Boid> redBoids = new Array<>();
	Array<Resource> blueResource = new Array<>();
	Array<Resource> greenResource = new Array<>();
	Array<Resource> redResource = new Array<>();
	Array<Queen> queens = new Array<>();
	Array<Scout> scouts = new Array<>();

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);

		// load resources and sounds
		loadResources();

		comparator = new Comparator<Boid>() {
			@Override
			public int compare(Boid o1, Boid o2) {
				return (o1.x == o2.x ? ( (o1.y == o2.y) ? 0 : (o1.y < o2.y) ? -1 : 1) : ((o1.x < o2.x) ? -1 : 1));
			}
		};

		// create blue boids objects
		for(int i = 0;i < 500;i++) {
			blueBoids.add(new Boid());
		}

		// create green boids objects
		for(int i = 0;i < 500;i++) {
			greenBoids.add(new Boid());
		}

		// create red boids objects
		for(int i = 0;i < 500;i++) {
			redBoids.add(new Boid());
		}

		// create queens objects
		for(int i = 0;i < 1;i++) {
			queens.add(new Queen());
		}

		// create scouts objects
		for(int i = 0;i < 0;i++) {
			scouts.add(new Scout());
		}

		// create blue resource objects
		for(int i = 0;i < 7;i++) {
			blueResource.add(new Resource());
		}

		// create green resource objects
		for(int i = 0;i < 7;i++) {
			greenResource.add(new Resource());
		}

		// create red resource objects
		for(int i = 0;i < 7;i++) {
			redResource.add(new Resource());
		}
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();

		// move boids
		for(int i = 0; i < blueBoids.size; i++){
			blueBoids.get(i).move();
			if(blueBoids.get(i).becomeQueen()){
				Queen q = new Queen();
				q.x = blueBoids.get(i).x;
				q.y = blueBoids.get(i).y;
				queens.add(q);
				blueBoids.removeValue(blueBoids.get(i),true);
			}
		}

		// move boids
		for(int i = 0; i < greenBoids.size; i++){
			greenBoids.get(i).move();
			if(greenBoids.get(i).becomeQueen()){
				Queen q = new Queen();
				q.x = greenBoids.get(i).x;
				q.y = greenBoids.get(i).y;
				queens.add(q);
				greenBoids.removeValue(greenBoids.get(i),true);
			}
		}

		// move boids
		for(int i = 0; i < redBoids.size; i++){
			redBoids.get(i).move();
			if(redBoids.get(i).becomeQueen()){
				Queen q = new Queen();
				q.x = redBoids.get(i).x;
				q.y = redBoids.get(i).y;
				queens.add(q);
				redBoids.removeValue(redBoids.get(i),true);
			}
		}

		// move queens
		for(int i = 0;i < queens.size;i++){
			queens.get(i).move();
			if(queens.get(i).spendResource()){
				int what = MathUtils.random(0,2);
				Boid b = new Boid();
				b.x = queens.get(i).x;
				b.y = queens.get(i).y;
				switch (what) {
					case 0:
						blueBoids.add(b);
						break;
					case 1:
						greenBoids.add(b);
						break;
					case 2:
						redBoids.add(b);
						break;
				}
			}
			if(!queens.get(i).isAlive){
				Queen q = queens.get(i);
				queens.removeValue(q,true);
			}
		}

		// move scouts
		for(int i = 0;i < scouts.size;i++){
			scouts.get(i).move();
		}

		// move blue resources
		for(int i = 0;i < blueResource.size;i++){
			if(!blueResource.get(i).isAlive){
				blueResource.set(i,new Resource());
			}
			blueResource.get(i).move();
		}

		// move green resources
		for(int i = 0;i < greenResource.size;i++){
			if(!greenResource.get(i).isAlive){
				greenResource.set(i,new Resource());
			}
			greenResource.get(i).move();
		}

		// move red resources
		for(int i = 0;i < redResource.size;i++){
			if(!redResource.get(i).isAlive){
				redResource.set(i,new Resource());
			}
			redResource.get(i).move();
		}

		// Проверяем достигли ли боиды цели
		checkAimOverlaps(blueBoids,blueResource,"blue");
		checkAimOverlaps(greenBoids,greenResource,"green");
		checkAimOverlaps(redBoids,redResource,"red");

		// Сортируем кординаты боидов
		blueBoids.sort(comparator);
		greenBoids.sort(comparator);
		redBoids.sort(comparator);

		// Передаем данные между боидами
		for(int i = 0; i < blueBoids.size; i++){
			updatePos(i,blueBoids);
		}
		for(int i = 0; i < greenBoids.size; i++){
			updatePos(i,greenBoids);
		}
		for(int i = 0; i < redBoids.size; i++){
			updatePos(i,redBoids);
		}

		for(int i = 0; i < blueBoids.size; i++){
			batch.draw(imgBlueBoid, blueBoids.get(i).x - blueBoids.get(i).innerWidth/2, blueBoids.get(i).y - blueBoids.get(i).innerHeight/2, blueBoids.get(i).innerWidth, blueBoids.get(i).innerHeight);
		}

		for(int i = 0; i < greenBoids.size; i++){
			batch.draw(imgGreenBoid, greenBoids.get(i).x - greenBoids.get(i).innerWidth/2, greenBoids.get(i).y - greenBoids.get(i).innerHeight/2, greenBoids.get(i).innerWidth, greenBoids.get(i).innerHeight);
		}

		for(int i = 0; i < redBoids.size; i++){
			batch.draw(imgRedBoid, redBoids.get(i).x - redBoids.get(i).innerWidth/2, redBoids.get(i).y - redBoids.get(i).innerHeight/2, redBoids.get(i).innerWidth, redBoids.get(i).innerHeight);
		}

		for(int i = 0;i < queens.size;i++){
			batch.draw(imgQueen,queens.get(i).x - queens.get(i).width/2,queens.get(i).y - queens.get(i).height/2,queens.get(i).width,queens.get(i).height);
			batch.draw(imgStorageQueen,queens.get(i).x - queens.get(i).storageWidth/2,queens.get(i).y - queens.get(i).storageHeight/2,queens.get(i).storageWidth,queens.get(i).storageHeight);
		}

		for(int i = 0;i < scouts.size;i++){
			batch.draw(imgBoid,scouts.get(i).x - scouts.get(i).innerWidth/2,scouts.get(i).y - scouts.get(i).innerHeight/2,scouts.get(i).innerWidth,scouts.get(i).innerHeight);
		}

		for(int i = 0;i < blueResource.size;i++){
			batch.draw(imgBlueResource,blueResource.get(i).x - blueResource.get(i).width/2,blueResource.get(i).y - blueResource.get(i).height/2,blueResource.get(i).width,blueResource.get(i).height);
		}

		for(int i = 0;i < greenResource.size;i++){
			batch.draw(imgGreenResource,greenResource.get(i).x - greenResource.get(i).width/2,greenResource.get(i).y - greenResource.get(i).height/2,greenResource.get(i).width,greenResource.get(i).height);
		}

		for(int i = 0;i < redResource.size;i++){
			batch.draw(imgRedResource,redResource.get(i).x - redResource.get(i).width/2,redResource.get(i).y - redResource.get(i).height/2,redResource.get(i).width,redResource.get(i).height);
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
		imgBlueBoid = new Texture("blue_boid.png");
		imgRedBoid = new Texture("red_boid.png");
		imgGreenBoid = new Texture("green_boid.png");
		imgQueen = new Texture("queen.png");
		imgStorageQueen = new Texture("storage_queen.png");
		imgBlueResource = new Texture("blue_resource.png");
		imgRedResource = new Texture("red_resource.png");
		imgGreenResource = new Texture("green_resource.png");
	}

	public void updatePos(int i,Array<Boid> boids){
		int j = lowerBound(boids,boids.get(i).x - boids.get(i).radius);
		for(; j < boids.size; j++) {
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
					updatePos(j,boids);
				}
			}
			if(boids.get(j).x > boids.get(i).x + boids.get(i).radius){
				break;
			}
		}
	}

	public void checkAimOverlaps(Array<Boid> boids,Array<Resource> resource,String type){
		for(int i = 0;i < boids.size;i++){
			for(int j = 0;j < queens.size;j++){
				if(boids.get(i).overlaps(queens.get(j))){
					Boid b = boids.get(i);
					b.distanceA = 0;
					if(b.isNeedA){
						b.isNeedA = false;
						b.isNeedB = true;
						queens.get(j).getResource(type);
						b.a = b.a + 180;
					}
					boids.set(i,b);
					break;
				}
			}
			for(int j = 0;j < resource.size;j++){
				if(boids.get(i).overlaps(resource.get(j))){
					Boid b = boids.get(i);
					b.distanceB = 0;
					if(b.isNeedB){
						b.isNeedB = false;
						b.isNeedA = true;
						resource.get(j).giveResource();
						b.a = b.a + 180;
					}
					boids.set(i,b);
					break;
				}
			}
		}
	}

	public int lowerBound(Array<Boid> b,float x){
		int l = -1,r = b.size-1;
		while (l+1 < r){
			int m = ((l+r)/2);
			if(b.get(m).x < x){
				l = m;
			}
			else{
				r = m;
			}
		}
		return r;
	}
}
