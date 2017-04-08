import { exec, ErrorCallback, SuccessCallback } from './utils';
import { Auth } from './auth';
import { Database } from './database';
import { Messaging } from './messaging';
import { Analytics } from './analytics';
import { RemoteConfig } from './remote-config';

export class App {
  static DEFAULT_NAME = '[DEFAULT]';

  private _ready = false;
  private _authInstance: Auth;
  private _databaseInstance: Database;
  // private _storageInstance: Storage;

  /**
   * @internal
   */
  _whenReady: Promise<void>;

  /**
   * @internal
   */
  constructor(public options: AppOptions = {}, private _name = App.DEFAULT_NAME) {
    this._whenReady = new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const callback = () => {
        this._ready = true;
        resolve();
      };
      exec(callback, reject, 'Firebase', 'initializeApp', [_name, options]);
    }).catch((reason: any) => {
      console.error(`Error initializing app ${_name}:`, reason);
    });
  }

  /**
   * @internal
   */
  _exec(success: SuccessCallback, error: ErrorCallback, action: string, args: any[] = []) {
    this._whenReady.then(() => exec(success, error, 'Firebase', action, [this._name, ...args]));
  }

  /**
   * @internal
   */
  _execWithoutName(success: SuccessCallback, error: ErrorCallback, action: string, args: any[] = []) {
    this._whenReady.then(() => exec(success, error, 'Firebase', action, args));
  }

  get name(): string {
    return this._name;
  }

  get isReady(): boolean {
    return this._ready;
  }

  auth(): Auth {
    if (!this._authInstance) {
      this._authInstance = new Auth(this);
    }
    return this._authInstance;
  }

  database(): Database {
    if (!this._databaseInstance) {
      this._databaseInstance = new Database(this);
    }
    return this._databaseInstance;
  }

  // storage(url?: string): Storage {
  //   if (!this._storageInstance) {
  //     this._storageInstance = new Storage(this, url);
  //   }
  //   return this._storageInstance;
  // }

  analytics(): Analytics {
    if (this._name !== App.DEFAULT_NAME) {
      throw new Error('You can only use Analytics on the default app');
    }
    return Analytics.getInstance(this);
  }

  messaging(): Messaging {
    if (this._name !== App.DEFAULT_NAME) {
      throw new Error('You can only use Messaging on the default app');
    }
    return Messaging.getInstance(this);
  }

  remoteConfig(): RemoteConfig {
    if (this._name !== App.DEFAULT_NAME) {
      throw new Error('You can only use RemoteConfig on the default app');
    }
    return RemoteConfig.getInstance(this);
  }

  delete(): Promise<any> {
    return new Promise<void>((resolve, reject) => {
      this._exec(() => resolve(), (reason?: any) => reject(reason), 'deleteApp');
    });
  }

}

export interface AppOptions {
  apiKey?: string;
  databaseURL?: string;
  androidAppId?: string;
  storageBucket?: string;
  messagingSenderId?: string;
}