import { App } from '../app';
export declare class Analytics {
    private _app;
    private static _instance;
    private constructor(_app);
    static getInstance(app: App): Analytics;
    private _exec(success, error, action, args);
    logEvent(name: string, params: LogEventParams): Promise<any>;
    setScreenName(name: string): Promise<any>;
    setUserId(id: string): Promise<any>;
    setUserProperty(name: string, value: string): Promise<any>;
}
export interface LogEventParams {
    [k: string]: any;
}
