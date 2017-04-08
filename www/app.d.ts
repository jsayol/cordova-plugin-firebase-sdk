import { Auth } from './auth';
import { Database } from './database';
import { Messaging } from './messaging';
import { Analytics } from './analytics';
import { RemoteConfig } from './remote-config';
export declare class App {
    options: AppOptions;
    private _name;
    static DEFAULT_NAME: string;
    private _ready;
    private _authInstance;
    private _databaseInstance;
    readonly name: string;
    readonly isReady: boolean;
    auth(): Auth;
    database(): Database;
    analytics(): Analytics;
    messaging(): Messaging;
    remoteConfig(): RemoteConfig;
    delete(): Promise<any>;
}
export interface AppOptions {
    apiKey?: string;
    databaseURL?: string;
    androidAppId?: string;
    storageBucket?: string;
    messagingSenderId?: string;
}
