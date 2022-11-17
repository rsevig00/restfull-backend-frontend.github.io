import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { UsuarioService } from './usuario.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  url = 'http://localhost:8080/users/auth'; // Ejecucion en local
  //url = '/users/auth'; // Ejecucion en docker
  token: any;

  constructor(private http: HttpClient, private router: Router, private _userService: UsuarioService) { }


  login(username: string, password: string) {
    this.http.post(this.url + '/signin', { username: username, password: password })
      .subscribe(async resp => {

        await this.navigateAndTokenAsync(resp);
      },
        (err: any) => {
          alert("No existe ningún usuario con estos datos");
        });

    // this.http.post(this.url + '/signin', {username: username, password: password})

  }

  navigateAndTokenAsync(resp: any) {
    this.router.navigate(['/home']);
    localStorage.setItem('activeID', resp.id);
    localStorage.setItem('auth_token', resp.accessToken);
    console.log(localStorage.getItem('auth_token'));
  }
}
