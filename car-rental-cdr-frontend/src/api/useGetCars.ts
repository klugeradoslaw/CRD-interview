import { useQuery } from "@tanstack/react-query";

const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export interface Car {
    id: number | string;
    brand: string;
    model: string;
    type: string;
    numberOfSeats: number;
}

const fetchAvailableCars = async (startDate: string | null, endDate: string | null): Promise<Car[]> => {
    if (!startDate || !endDate) return [];

    const url = `${BASE_URL}/cars/available?startDate=${encodeURIComponent(startDate)}&endDate=${encodeURIComponent(endDate)}`;
    const response = await fetch(url);

    if (!response.ok) {
        throw new Error(`Błąd (${response.status}). Skontaktuj się administratorem.`);
    }

    return response.json();
};

export const useGetCars = (startDate: string | null, endDate: string | null) => {
    return useQuery({
        queryKey: ["cars/available", startDate, endDate],
        queryFn: () => fetchAvailableCars(startDate, endDate),
        enabled: Boolean(startDate && endDate),
    });
};

export default useGetCars;