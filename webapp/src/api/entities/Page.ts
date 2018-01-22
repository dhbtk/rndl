export default interface Page<T> {
    total: number;
    page: number;
    size: number;
    content: T[];
}