import { Injectable } from '@angular/core';
import { Usuario } from '../interfaces/usuario';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http'
import { Observable, Subject } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  listUsuarios: Usuario[] = [];
  usuarioActivo: String | undefined;

  private usersUrl: string;
  private headers = new HttpHeaders().set('Authorization', `Bearer ${localStorage.getItem("auth_token")}`);

  constructor(private http: HttpClient) {
    this.usersUrl = '/api/users';
  }

  getUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.usersUrl, { headers: this.headers });
  }
  eliminarUsuario(id: number) {
    return this.http.delete<number>(this.usersUrl+"/"+id, { headers: this.headers });
  }
  agregarUsuario(user: Usuario) {

    console.log("Usuario del serevicio", user)
    return this.http.post(this.usersUrl, user, { headers: this.headers }).subscribe();
  }

  editarUsuario(usuario: Usuario) {
    let params = new HttpParams();
    return this.http.put(this.usersUrl, usuario, { headers: this.headers }).subscribe();
  }

  actualizarUsuarioActivo(usuarioName: String) {
    this.usuarioActivo = usuarioName;
  }

}