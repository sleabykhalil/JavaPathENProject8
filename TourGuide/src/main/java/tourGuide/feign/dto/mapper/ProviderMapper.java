package tourGuide.feign.dto.mapper;

import tourGuide.feign.dto.tripPricerDto.ProviderDto;
import tripPricer.Provider;

import java.util.ArrayList;
import java.util.List;

public class ProviderMapper {
    public Provider providerDtoToProvider(ProviderDto providerDto) {
        return new Provider(providerDto.tripId, providerDto.name, providerDto.price);
    }

    public List<Provider> providerDtoListToProviderList(List<ProviderDto> providerDtoList) {
        List<Provider> providerList = new ArrayList<>();
        for (ProviderDto providerDto : providerDtoList) {
            providerList.add(providerDtoToProvider(providerDto));
        }
        return providerList;
    }

    public ProviderDto providerToProviderDto(Provider provider) {
        return new ProviderDto(provider.name, provider.price, provider.tripId);
    }

    public List<ProviderDto> providerListToProviderDtoList(List<Provider> providerList) {
        List<ProviderDto> providerDtoList = new ArrayList<>();
        for (Provider provider : providerList) {
            providerDtoList.add(providerToProviderDto(provider));
        }
        return providerDtoList;
    }
}
