export interface Food {
    id?: number;
    name: string;
}

const API_BASE = 'http://localhost:8080/api/food';

export const FoodService = {
    async getAll(): Promise<Food[]> {
        const res = await fetch(API_BASE);
        return res.json();
    },

    async add(food: Food): Promise<Food> {
        const res = await fetch(API_BASE, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(food)
        });
        return res.json();
    },

    async delete(id: number): Promise<void> {
        await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
    },

    async getRandom(): Promise<Food> {
        const res = await fetch(`${API_BASE}/random`);
        return res.json();
    }
};
