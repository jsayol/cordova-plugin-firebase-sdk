import { SuccessCallback, ErrorCallback } from '../utils';
import { App } from '../app';
export declare class RemoteConfig {
    private _app;
    private static _instance;
    private constructor(_app);
    static getInstance(app: App): RemoteConfig;
    private _exec(success, error, action, args?);
    activateFetched(resolve: SuccessCallback, reject: ErrorCallback): void;
    fetch(cacheExpirationSeconds?: number): Promise<any>;
    getByteArray(key: string, namespace?: string): Promise<any>;
    getValue(key: string, namespace?: string): Promise<any>;
    getInfo(): Promise<ConfigInfo>;
    setConfigSettings(settings: ConfigSettings): Promise<any>;
    setDefaults(defaults: ConfigDefaults, namespace?: string): Promise<any>;
}
export interface ConfigSettings {
    developerModeEnabled?: boolean;
}
export interface ConfigDefaults {
    [k: string]: any;
}
export interface ConfigInfo {
    configSettings: ConfigSettings;
    fetchTimeMillis: number;
    lastFetchStatus: number;
}
