import { Component, DoCheck, KeyValueDiffers, KeyValueDiffer } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Player} from "./shared/models/player.model";
import {GameApiCollection} from "./shared/models/gameapicollection.model";
import {PlayerApiCollection} from "./shared/models/playerapicollection.model";
import {Game} from "./shared/models/game.model";
import {debug} from "util";
import {PlayingField} from "./shared/models/playingField.model";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    title = 'Testing';
	players: PlayerApiCollection;
	player: Player;
	games:GameApiCollection;
	game:Game;
	winner:Player = null;
	message = '';
	error = null;
	
	fieldState: number[][] = [];
	
	differ: KeyValueDiffer<string, any>;
	constructor(private http: HttpClient, private differs: KeyValueDiffers) {
		this.differ = this.differs.find({}).create();
		this.http = http;
		
		this.retrievePlayers(http);
		this.retrievePlayer(http, 1);
		this.retrieveGames(http);

		this.retrieveLastGame();//this.games.games[this.games.games.length-1].id);
		this.message="try this now";
	}
	
	ngDoCheck() {
		const change = this.differ.diff(this);
		if (change) {
			change.forEachChangedItem(item => {
				console.log('item changed', item);
			});
		}
	}
 
	public retrievePlayers(http: HttpClient) {
		http.get('api/players').subscribe(data => {
			this.players = new PlayerApiCollection().deserialize(data);
		});
	}
	
	public retrievePlayer(http: HttpClient, playerId:number) {
		http.get('api/players/'+playerId).subscribe(data => {
			this.player = new Player().deserialize(data);
		});
	}
	
	public retrieveGames(http: HttpClient) {
		http.get('api/games').subscribe(data => {
			this.games = new GameApiCollection().deserialize(data);
		});
	}
	
	public retrieveLastGame() {
		this.http.get('api/games').subscribe(data => {
			this.games = new GameApiCollection().deserialize(data);
			
			let sortedgames=this.games.games.sort(function(a, b){return a.id-b.id});
			
			this.game = sortedgames[sortedgames.length-1];
		});
	}
	
	public setGame(game:Game){
		this.game = game;
		
		this.setFieldState(game.playingField);
		
	}
	
	public setFieldState(field:PlayingField) {
		let temp:number[][] = [];
		for(let i=0;i<6;i++){
			
			temp[i] = [];
			for (let j=0;j<6;j++) {
				
				temp[i][j] = 0;
			}
		}
		
		field.places.forEach((col, j) => {
			
			col.forEach((value, i) => {
				
				temp[i][j] = value;
			});
		});
		
		this.fieldState = temp;
	}
	
	public doMove(col:number) {
		var post =this.http.post('games/move/'+this.game.id+'/'+col, {
		
		});
		post.subscribe(data => {
			let game:Game = new Game().deserialize(data);
			
			this.setFieldState(new PlayingField().deserialize(data['playingField']));
			this.game = game;
			this.retrieveWinner()
		}, error => this.error = error);
		
	}
	
	public doSurrender() {
		var post =this.http.post('games/surrender/'+this.game.id, null);
		post.subscribe(data => {
			this.message = "surrendered";
		})
		this.message = "surrendered2";
	}
	
	public doNewGame() {
		
		var post =this.http.post('games/newgame/1/2', null);
		post.subscribe(data => {
			 let game:Game = new Game().deserialize(data);
			 this.game = game;
			 this.setFieldState(game.playingField);
			
		})
	}
	
	public retrieveWinner() {
		////console.debug("hallo", this.game['winner']);
		// this.http.get(this.game._links['winner'].href).subscribe(data => {
		// 	this.winner = new Player().deserialize(data);
		// });
		this.winner = this.game['winner'];
		this.message = this.game['winner']['id'] + ' | ' + this.game['winner']['username']
	}
	
	public doMoveTest(col:number) {
		this.http.get('games/movetest').subscribe(data => {
		
		});
		
	}
	
}