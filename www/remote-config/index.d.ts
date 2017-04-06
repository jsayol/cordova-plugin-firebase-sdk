import { SuccessCallback, ErrorCallback } from '../utils';
export declare class RemoteConfig {
    private static _instance;
    constructor();
    static getInstance(): RemoteConfig;
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
export declare const remoteConfig: () => RemoteConfig;
