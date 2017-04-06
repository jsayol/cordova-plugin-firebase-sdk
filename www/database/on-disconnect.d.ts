export declare class OnDisconnect {
    private _path;
    constructor(_path: string);
    set(value: any): Promise<void>;
    remove(): Promise<void>;
    update(value: any): Promise<void>;
    cancel(): Promise<void>;
    setWithPriority(value: any, priority: number | string | null): Promise<void>;
}
