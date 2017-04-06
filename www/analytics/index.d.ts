export declare class Analytics {
    private static _instance;
    constructor();
    static getInstance(): Analytics;
    logEvent(name: string, params: LogEventParams): Promise<any>;
    setScreenName(name: string): Promise<any>;
    setUserId(id: string): Promise<any>;
    setUserProperty(name: string, value: string): Promise<any>;
}
export interface LogEventParams {
    [k: string]: any;
}
export declare const analytics: () => Analytics;
