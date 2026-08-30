import { useMutation, useQueryClient } from "@tanstack/react-query";

const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export interface CreateReservationDto {
    carId: number | string;
    startDateTime: string;
    endDateTime: string;
}

export interface ReservedCar {
    id: number | string;
    type?: string;
    brand: string;
    model: string;
}

export interface ReservationResponse {
    id: number | string;
    car: ReservedCar;
    startDateTime: string;
    endDateTime: string;
}

const postReservation = async (data: CreateReservationDto): Promise<ReservationResponse> => {
    const response = await fetch(`${BASE_URL}/reservations`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        const errorData = await response.json().catch(() => null);
        const errorMessage = errorData?.message || `Błąd (${response.status}) podczas tworzenia rezerwacji.`;
        throw new Error(errorMessage);
    }

    return response.json();
};

export const usePostReservation = () => {
    const queryClient = useQueryClient();

    return useMutation<ReservationResponse, Error, CreateReservationDto>({
        mutationFn: postReservation,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["cars/available"] });
        },
    });
};

export default usePostReservation;